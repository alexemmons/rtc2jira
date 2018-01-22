package to.rtc.rtc2jira.exporter.jira;

import static to.rtc.rtc2jira.storage.Field.of;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

import to.rtc.rtc2jira.ExportManager;
import to.rtc.rtc2jira.Settings;
import to.rtc.rtc2jira.exporter.Exporter;
import to.rtc.rtc2jira.exporter.jira.entities.BulkCreateContainer;
import to.rtc.rtc2jira.exporter.jira.entities.BulkCreateEntry;
import to.rtc.rtc2jira.exporter.jira.entities.BulkCreateResponseEntity;
import to.rtc.rtc2jira.exporter.jira.entities.Issue;
import to.rtc.rtc2jira.exporter.jira.entities.IssueAttachment;
import to.rtc.rtc2jira.exporter.jira.entities.IssueComment;
import to.rtc.rtc2jira.exporter.jira.entities.IssueFields;
import to.rtc.rtc2jira.exporter.jira.entities.IssueResolution;
import to.rtc.rtc2jira.exporter.jira.entities.IssueSearch;
import to.rtc.rtc2jira.exporter.jira.entities.IssueSearch.IssueSearchResult;
import to.rtc.rtc2jira.exporter.jira.entities.IssueStatus;
import to.rtc.rtc2jira.exporter.jira.entities.IssueType;
import to.rtc.rtc2jira.exporter.jira.entities.JiraUser;
import to.rtc.rtc2jira.exporter.jira.entities.Project;
import to.rtc.rtc2jira.exporter.jira.entities.ResolutionEnum;
import to.rtc.rtc2jira.exporter.jira.entities.StateEnum;
import to.rtc.rtc2jira.exporter.jira.mapping.MappingRegistry;
import to.rtc.rtc2jira.exporter.jira.mapping.WorkItemTypeMapping;
import to.rtc.rtc2jira.storage.Attachment;
import to.rtc.rtc2jira.storage.AttachmentStorage;
import to.rtc.rtc2jira.storage.Comment;
import to.rtc.rtc2jira.storage.FieldNames;
import to.rtc.rtc2jira.storage.StorageEngine;
import to.rtc.rtc2jira.storage.StorageQuery;

public class JiraExporter implements Exporter {
  private static final String DUMMY_SUMMARY_TEXT = "Dummy";
  private static final Logger LOGGER = Logger.getLogger(JiraExporter.class.getName());
  public static final JiraExporter INSTANCE;
  private StorageEngine store;
  private Settings settings;
  private JiraRestAccess restAccess;
  private Optional<Project> projectOptional;
  private int highestExistingId = -1;
  private MappingRegistry mappingRegistry;
  private WorkItemTypeMapping workItemTypeMapping;
  private Set<Integer> updatedItems = new HashSet<Integer>();

  static private Set<Integer> _tempMovedItems = new HashSet<Integer>();

  static {
    INSTANCE = new JiraExporter();
    LOGGER.addHandler(ExportManager.DEFAULT_LOG_HANDLER);
    // florian
//    _tempMovedItems.add(Integer.valueOf(35392));
//    _tempMovedItems.add(Integer.valueOf(36385));
//    _tempMovedItems.add(Integer.valueOf(36068));
//    // franz
//    _tempMovedItems.add(Integer.valueOf(30379));
//    _tempMovedItems.add(Integer.valueOf(31428));
//    _tempMovedItems.add(Integer.valueOf(31858));
//    _tempMovedItems.add(Integer.valueOf(34034));
//    _tempMovedItems.add(Integer.valueOf(34035));
//    _tempMovedItems.add(Integer.valueOf(34477));
//    _tempMovedItems.add(Integer.valueOf(34549));
//    _tempMovedItems.add(Integer.valueOf(34663));
//    _tempMovedItems.add(Integer.valueOf(34887));
//    _tempMovedItems.add(Integer.valueOf(34888));
//    _tempMovedItems.add(Integer.valueOf(34889));
//    _tempMovedItems.add(Integer.valueOf(34890));
//    _tempMovedItems.add(Integer.valueOf(34892));
//    _tempMovedItems.add(Integer.valueOf(34893));
//    _tempMovedItems.add(Integer.valueOf(34895));
//    _tempMovedItems.add(Integer.valueOf(32606));
//    _tempMovedItems.add(Integer.valueOf(35964));
//    _tempMovedItems.add(Integer.valueOf(36132));
//    _tempMovedItems.add(Integer.valueOf(36133));
//    _tempMovedItems.add(Integer.valueOf(36134));
//    _tempMovedItems.add(Integer.valueOf(36135));
//    _tempMovedItems.add(Integer.valueOf(36189));
//    _tempMovedItems.add(Integer.valueOf(36192));
//    _tempMovedItems.add(Integer.valueOf(36193));
//    _tempMovedItems.add(Integer.valueOf(36194));
//    _tempMovedItems.add(Integer.valueOf(36241));
//    _tempMovedItems.add(Integer.valueOf(36242));
//    _tempMovedItems.add(Integer.valueOf(36284));
//    _tempMovedItems.add(Integer.valueOf(36480));
//    _tempMovedItems.add(Integer.valueOf(36544));
//    _tempMovedItems.add(Integer.valueOf(36696));
//    _tempMovedItems.add(Integer.valueOf(36761));
//    _tempMovedItems.add(Integer.valueOf(36774));
//    _tempMovedItems.add(Integer.valueOf(36793));
//
//    // unknown
//    _tempMovedItems.add(Integer.valueOf(33815));
//    _tempMovedItems.add(Integer.valueOf(36800));
//    _tempMovedItems.add(Integer.valueOf(36801));
//    // 19042016
//    _tempMovedItems.add(Integer.valueOf(36751));
//    _tempMovedItems.add(Integer.valueOf(36811));

  }

  private JiraExporter() {};

  @Override
  public boolean isConfigured() {
    return Settings.getInstance().hasJiraProperties();
  }

  @Override
  public void initialize(Settings settings, StorageEngine store) throws Exception {
    this.settings = settings;
    this.store = store;
    setRestAccess(new JiraRestAccess(settings.getJiraUrl(), settings.getJiraUser(), settings.getJiraPassword()));
    ClientResponse response = getRestAccess().get("/myself");
    // ClientResponse response = getRestAccess().get("/issue/WOR-137");

    if (response.getStatus() != Status.OK.getStatusCode()) {
      throw new RuntimeException("Unable to connect to jira repository: " + response.toString());
    }
    this.projectOptional = getProject();
    mappingRegistry = new MappingRegistry();
    this.workItemTypeMapping = new WorkItemTypeMapping();
  }

  public void createOrUpdateItem(int rtcId) throws Exception {
    ODocument workItem = StorageQuery.getRTCWorkItem(this.store, rtcId);
    createOrUpdateItem(workItem);
  }

  @Override
  public void createOrUpdateItem(ODocument item) throws Exception {
    int workItemId = Integer.parseInt(item.field(FieldNames.ID));
    ensureWorkItemWithId(workItemId);
    Date modified = StorageQuery.getField(item, FieldNames.MODIFIED, Date.from(Instant.now()));
    Date lastExport = StorageQuery.getField(item, FieldNames.JIRA_EXPORT_TIMESTAMP, new Date(0));
    if (Settings.getInstance().isForceUpdate() || modified.compareTo(lastExport) > 0) {
      if (!"SRVS Management".equals(item.field(FieldNames.PROJECT_AREA)) && !"DevIT SCM".equals(item.field(FieldNames.PROJECT_AREA))
          && !_tempMovedItems.contains(Integer.valueOf(workItemId))) {
        updateItem(item);
        updatedItems.add(Integer.valueOf(workItemId));
      }
    }
  }

  private void ensureWorkItemWithId(int workItemId) throws Exception {
    // get current highest id from server, if necessary
    if (highestExistingId == -1) {
      IssueSearchResult searchResult =
          IssueSearch.INSTANCE.run("project = '" + settings.getJiraProjectKey() + "' ORDER BY id DESC");
      if (searchResult.getTotal() > 0) {
        Issue last = searchResult.getIssues().get(0);
        highestExistingId = extractId(last.getKey());
      } else {
        highestExistingId = 0;
      }
    }

    // set target to total export item count, if necessary
    int totalItemsToExport = Settings.getInstance().getTotalItems();
    if (totalItemsToExport > 0 && totalItemsToExport > workItemId) {
      workItemId = totalItemsToExport;
    }

    while (highestExistingId < workItemId) {
      int gap = workItemId - highestExistingId;
      gap = (gap <= 100) ? gap : 100;
      createDummyIssues(gap);
    }
  }

  private void createDummyIssues(int total) throws Exception {
    if (projectOptional.isPresent()) {
      // build request entity
      Project project = projectOptional.get();
      BulkCreateContainer postEntity = new BulkCreateContainer();
      List<BulkCreateEntry> issueUpdates = postEntity.getIssueUpdates();
      for (int i = 0; i < total; i++) {
        Issue issue = new Issue();
        IssueFields fields = issue.getFields();
        fields.setProject(project);
        fields.setIssuetype(workItemTypeMapping.getIssueType(IssueType.TASK.getName(), project));
        fields.setSummary(DUMMY_SUMMARY_TEXT);
        fields.setDescription("This is just a dummy issue. Delete it after successfully migrating to Jira.");
        issueUpdates.add(new BulkCreateEntry(fields));
      }
      // post request
      long startTime = System.currentTimeMillis();
      LOGGER.log(Level.INFO, "Starting bulk creation of " + total + " items.");
      ClientResponse postResponse = getRestAccess().post("/issue/bulk", postEntity);
      if (postResponse.getStatus() == Status.CREATED.getStatusCode()) {
        BulkCreateResponseEntity respEntity = postResponse.getEntity(BulkCreateResponseEntity.class);
        List<Issue> issues = respEntity.getIssues();
        if (!issues.isEmpty()) {
          highestExistingId = extractId(issues.get(issues.size() - 1).getKey());
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double minutes = Math.floor(duration / (1000 * 60));
        double seconds = (duration % (1000 * 60)) / 1000;
        LOGGER.log(Level.INFO, "Bulk creation of " + issues.size() + " items took " + (int) minutes + " min. and "
            + (int) seconds + " sec.");
      } else {
        String errorMessage = "Problems while bulk creating issues: " + postResponse.getEntity(String.class);
        throw new Exception(errorMessage);
      }
    }
  }

  private void updateItem(ODocument item) throws Exception {
    if (projectOptional.isPresent()) {
      Project project = projectOptional.get();
      Issue issue = createIssueFromWorkItem(item, project);
      if (issue != null) {
        persistIssue(item, issue);
        persistNewComments(item, issue);
        try {
          persistAttachments(item, issue);
        } catch (IOException e) {
          throw new Exception("Fatal error - could not open attachment directory while exporting", e);
        }
      }
    }
  }

  private String getKey(ODocument item) {
    String key = null;
    if (projectOptional.isPresent()) {
      String id = item.field(FieldNames.ID);
      key = getIssueKey(id);
    }
    return key;
  }

  public String getIssueKey(String rtcId) {
    return settings.getJiraProjectKey() + '-' + rtcId;
  }

  public void updateIfStillDummy(String jiraKey) throws Exception {
    int rtcId = extractId(jiraKey);
    if (!this.updatedItems.contains(Integer.valueOf(rtcId))) {
      Issue issue = new Issue();
      issue.setKey(jiraKey);
      ClientResponse cr = getRestAccess().get(issue.getSelfPath());
      if (cr.getStatus() == 200) {
        issue = cr.getEntity(Issue.class);
        if (DUMMY_SUMMARY_TEXT.equals(issue.getFields().getSummary())) {
          createOrUpdateItem(rtcId);
        } else {
          updatedItems.add(Integer.valueOf(rtcId));
        }
      }
    }
  }

  int extractId(String key) {
    String[] split = key.split("-");
    return Integer.parseInt(split[1]);
  }

  private void persistIssue(ODocument item, Issue issue) {
    Issue lastExportedIssue = getLastExportedInfo(item, issue.getFields().getIssuetype());
    try {
      updateIssueInJira(issue, lastExportedIssue);
      storeReference(issue, item);
      cacheInfoOfLastExport(issue, item);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error while updating in Jira", e);
    }
  }

  private String migrateOldStatus(String oldStatusId, IssueType issueType) {
    String result = oldStatusId;
    if ("10000".equals(oldStatusId)) { // todo
      result = "1"; // open
      if (IssueType.BUSINESS_NEED.equals(issueType)) {
        result = "10103";
      }
    } else if (IssueType.BUG.equals(issueType)) {
      if ("10001".equals(oldStatusId)) { // done
        result = "6"; // verified
      }
    } else if (IssueType.EPIC.equals(issueType)) {
      // do nothing
    } else if (IssueType.USER_STORY.equals(issueType)) {
      // do nothing
    } else if (IssueType.STORY.equals(issueType)) {
      // do nothing
    } else if (IssueType.BUSINESS_NEED.equals(issueType)) {
      if ("10001".equals(oldStatusId)) { // done
        result = "6";
      }
    } else if (IssueType.IMPEDIMENT.equals(issueType)) {
      if ("10001".equals(oldStatusId)) { // done
        result = "5";
      }
    } else if (IssueType.TASK.equals(issueType)) {
      if ("10001".equals(oldStatusId)) { // done
        result = "5";
      }
    } else if (IssueType.SUB_TASK.equals(issueType)) {
      if ("10001".equals(oldStatusId)) { // done
        result = "5";
      }
    }

    return result;
  }


  private Issue getLastExportedInfo(ODocument item, IssueType issueType) {
    String lastExportedStatus = item.field(FieldNames.JIRA_LAST_EXPORTED_STATUS);
    lastExportedStatus = migrateOldStatus(lastExportedStatus, issueType);
    Issue lastExportedIssue = new Issue();
    // status
    lastExportedIssue.getFields().setStatus(IssueStatus.createStartingStatus(issueType));
    if (lastExportedStatus != null) {
      Optional<StateEnum> stateOpt = StateEnum.forJiraId(lastExportedStatus, issueType);
      stateOpt.ifPresent(se -> {
        lastExportedIssue.getFields().setStatus(se.getIssueStatus());
      });
      if (!stateOpt.isPresent()) {
        LOGGER.severe("No StateEnum found for last exported status, id = " + lastExportedStatus);
      }
    }
    return lastExportedIssue;
  }

  private void persistAttachments(ODocument item, Issue issue) throws IOException {
    AttachmentStorage storage = new AttachmentStorage();
    String id = item.field(FieldNames.ID);
    List<Attachment> attachments = storage.readAttachments(Long.parseLong(id));
    if (attachments.size() > 0) {
      List<String> alreadyExportedAttachments = getAlreadyExportedAttachments(issue);
      final FormDataMultiPart multiPart = new FormDataMultiPart();
      int newlyAdded = 0;
      for (Attachment attachment : attachments) {
        // check if already exported
        if (!alreadyExportedAttachments.contains(attachment.getPath().getFileName().toString())) {
          final File fileToUpload = attachment.getPath().toFile();
          if (fileToUpload != null) {
            multiPart.bodyPart(new FileDataBodyPart("file", fileToUpload, MediaType.APPLICATION_OCTET_STREAM_TYPE));
            newlyAdded++;
          }
        }
      }
      if (newlyAdded > 0) {
        try {
          getRestAccess().postMultiPart(issue.getSelfPath() + "/attachments", multiPart);
        } catch (Exception e) {
          LOGGER.severe("Could not upload attachments");
        }
      }
    }
  }

  List<String> getAlreadyExportedAttachments(Issue issue) {
    List<String> result = new ArrayList<String>();
    List<IssueAttachment> attachment = issue.getFields().getAttachment();
    if (attachment != null) {
      for (IssueAttachment issueAttachment : attachment) {
        result.add(issueAttachment.getFilename());
      }
    }
    return result;
  }


  private void persistNewComments(ODocument item, Issue issue) {
    List<IssueComment> issueComments = issue.getFields().getComment().getComments();
    List<Comment> comments = item.field(FieldNames.COMMENTS);
    if (comments != null) {
      for (Comment comment : comments) {
        IssueComment issueComment = IssueComment.createWithIdAndBody(issue, comment.getJiraId(), comment.getComment());
        if (comment.getJiraId() == null) {
          JiraUser jiraUser = persistUser(comment);
          issueComment.setAuthor(jiraUser);
          issueComment.setCreated(comment.getDate());
          ClientResponse cr = getRestAccess().post(issueComment.getPath(), issueComment);
          if (cr.getStatus() == 201) {
            IssueComment issueCommentPosted = cr.getEntity(IssueComment.class);
            issueComment.setId(issueCommentPosted.getId());
            issueCommentPosted.setIssue(issue);
            // update document comment
            comment.setJiraId(issueComment.getId());
            issueComments.add(issueComment);
          } else {
            String entity = cr.getEntity(String.class);
            LOGGER.severe("Could not add comment ****** " + comment.getComment()
                + " ******* to the issue with the key " + issue.getKey() + ". Response entity: " + entity);
          }
        }
      }
      // save comments in item because IDs may have been added
      store.setFields(item, //
          of(FieldNames.COMMENTS, comments));
    }
  }

  private JiraUser persistUser(Comment comment) {
    JiraUser jiraUser = JiraUser.createFromComment(comment);
    ClientResponse cr = getRestAccess().get(jiraUser.getSelfPath());
    if (!isResponseOk(cr)) {
      ClientResponse postResponse = getRestAccess().post(jiraUser.getPath(), jiraUser);
      if (isResponseOk(postResponse)) {
        jiraUser = postResponse.getEntity(JiraUser.class);
      }
    }
    return jiraUser;
  }

  void storeReference(Issue jiraIssue, ODocument workItem) {
    store.setFields(workItem, //
        of(FieldNames.JIRA_KEY_LINK, jiraIssue.getKey()), //
        of(FieldNames.JIRA_ID_LINK, jiraIssue.getId()));
  }

  void cacheInfoOfLastExport(Issue jiraIssue, ODocument workItem) {
    store.setFields(
        workItem, //
        of(FieldNames.JIRA_EXPORT_TIMESTAMP,
            StorageQuery.getField(workItem, FieldNames.MODIFIED, Date.from(Instant.now()))),
        of(FieldNames.JIRA_LAST_EXPORTED_STATUS, jiraIssue.getFields().getStatus().getId()));
  }

  private Optional<Project> getProject() {
    Project projectConfig = new Project();
    projectConfig.setKey(settings.getJiraProjectKey());
    return Optional.ofNullable(getRestAccess().get(projectConfig.getSelfPath(), Project.class));
  }

  Issue createIssueInJira(Issue issue) {
    ClientResponse postResponse = getRestAccess().post(issue.getPath(), issue);
    if (postResponse.getStatus() == Status.CREATED.getStatusCode()) {
      return postResponse.getEntity(Issue.class);
    } else {
      System.err.println("Problems while creating issue: " + postResponse.getEntity(String.class));
      return null;
    }
  }

  private void updateIssueInJira(Issue issue, Issue lastExportedIssue) throws Exception {
    // prepare status transition
    IssueType issueType = issue.getFields().getIssuetype();
    StateEnum targetStatus = issue.getFields().getStatus().getStatusEnum(issueType);
    StateEnum currentStatus = lastExportedIssue.getFields().getStatus().getStatusEnum(issueType);
    List<String> transitionPath = null;
    if (currentStatus.isEditable() && currentStatus == targetStatus) {
//      transitionPath = currentStatus.forceTransitionPath(targetStatus);
    	transitionPath = new ArrayList<>();
    } else {
      transitionPath = currentStatus.getTransitionPath(targetStatus);
    }
    // put issue in editable state
    if (currentStatus.isEditable() && transitionPath.size() > 0) {
      String intermediateTransitionId = transitionPath.remove(0);
      if (!doTransition(issue, intermediateTransitionId)) {
        throw new Exception();
      };
    }
    // send put request
    ClientResponse postResponse = getRestAccess().put("/issue/" + issue.getKey(), issue);
    if (!isResponseOk(postResponse)) {
      LOGGER.severe("Problems while updating issue: " + postResponse.getEntity(String.class));
      throw new Exception();
    } else {
      while (transitionPath.size() > 0) {
        String transitionId = transitionPath.remove(0);
        if (!StateEnum.NO_TRANSITION.equals(transitionId)) {
          if (!doTransition(issue, transitionId)) {
            // revert status
            issue.getFields().setStatus(lastExportedIssue.getFields().getStatus());
            throw new Exception();
          }
        } else if (targetStatus != currentStatus) {
          // revert status
          issue.getFields().setStatus(lastExportedIssue.getFields().getStatus());
          throw new Exception();
        }
      }
    }
  }

  private boolean doTransition(Issue issue, String transitionId) {
    String entity = "{\"transition\":{\"id\":" + transitionId + "}}";
    ClientResponse postResponse =
        getRestAccess().post("/issue/" + issue.getKey() + "/transitions?expand=transitions.fields", entity);
    if (isResponseOk(postResponse)) {
      return true;
    } else {
      LOGGER.severe("Problems while transitioning issue "+ issue.getKey() +": " + postResponse.getEntity(String.class));
      return false;
    }
  }

  /**
   * Returns null if cannot find corresponding jira issue
   * 
   * @param workItem
   * @param project
   * @return
   */
  Issue createIssueFromWorkItem(ODocument workItem, Project project) {
    Issue issue = new Issue();
    String id = workItem.field(FieldNames.ID);
    issue.setId(id);
    String key = getKey(workItem);
    issue.setKey(key);
    ClientResponse cr = getRestAccess().get(issue.getSelfPath());
    if (cr.getStatus() == 200) {
      issue = cr.getEntity(Issue.class);
      IssueFields issueFields = issue.getFields();
      String retrievedIssueKey = issue.getKey();
//      if (!retrievedIssueKey.startsWith("RTC")) {
//        LOGGER.log(Level.WARNING, "The issue " + key + " has been moved to another project: " + retrievedIssueKey);
//        issue = null;
//      } else {
        issueFields.setProject(project);
        store.setFields(workItem, of(FieldNames.JIRA_LAST_EXPORTED_STATUS, issueFields.getStatus().getId()));
        mappingRegistry.map(workItem, issue, store);
        // set resolution to appropriate default, otherwise it will be set to "fixed" whenever
        // status
        // is "done", even if issue is not a defect
        if (issueFields.getStatus().getStatusEnum(issue.getFields().getIssuetype()).isFinished()
            && issueFields.getResolution() == null) {
          issueFields.setResolution(new IssueResolution(ResolutionEnum.done));
        }
//      }
    } else {
      String issueKey = issue.getKey();
      LOGGER
          .log(
              Level.SEVERE,
              "A problem occurred while retrieving the issue with the key " + issueKey + " : "
                  + cr.getEntity(String.class));
      issue = null;
    }
    return issue;
  }


  private boolean isResponseOk(ClientResponse cr) {
    return cr.getStatus() >= Status.OK.getStatusCode() && cr.getStatus() <= Status.PARTIAL_CONTENT.getStatusCode();
  }

  public JiraRestAccess getRestAccess() {
    return restAccess;
  }

  private void setRestAccess(JiraRestAccess restAccess) {
    this.restAccess = restAccess;
  }

  @Override
  public void postExport() throws Exception {}

}
