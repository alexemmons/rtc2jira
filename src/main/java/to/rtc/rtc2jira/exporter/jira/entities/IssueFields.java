package to.rtc.rtc2jira.exporter.jira.entities;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonView;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = NON_NULL)
public class IssueFields {

  private IssueType issuetype;
  private ProjectOverview project;
  private String summary;
  private String description;
  private IssuePriority priority;
  private Date duedate;
  private IssueCommentContainer comment = new IssueCommentContainer();
  private Date updated;
  private IssueResolution resolution;
  private Date resolutiondate;
  private JiraUser creator;
  private JiraUser reporter;
  private JiraUser owner;
  private JiraUser assignee;
  private JiraUser resolver;
  private Date created;
  private List<IssueAttachment> attachment;
  private IssueStatus status;

  private String acceptanceCriteria;
  private List<String> labels = new ArrayList<String>();
  private int storyPoints;
  private Date rtcCreated;

  public IssueType getIssuetype() {
    return issuetype;
  }

  public void setIssuetype(IssueType issuetype) {
    this.issuetype = issuetype;
  }

  public ProjectOverview getProject() {
    return project;
  }

  public void setProject(ProjectOverview project) {
    this.project = project;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public IssuePriority getPriority() {
    return priority;
  }

  public void setPriority(IssuePriority priorty) {
    this.priority = priorty;
  }

  @XmlJavaTypeAdapter(JiraDateStringAdapter.class)
  @JsonView(IssueView.Update.class)
  public Date getDuedate() {
    return duedate;
  }

  public void setDuedate(Date duedate) {
    this.duedate = duedate;
  }

  @JsonView(IssueView.Read.class)
  public IssueCommentContainer getComment() {
    return comment;
  }

  public void setComment(IssueCommentContainer comment) {
    this.comment = comment;
  }

  @JsonView(IssueView.Read.class)
  public List<IssueAttachment> getAttachment() {
    return attachment;
  }

  public void setAttachment(List<IssueAttachment> attachment) {
    this.attachment = attachment;
  }

  @JsonView(IssueView.Read.class)
  public JiraUser getCreator() {
    return creator;
  }

  public void setCreator(JiraUser creator) {
    this.creator = creator;
  }

  @JsonView(IssueView.Read.class)
  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  @JsonView(IssueView.Read.class)
  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  @JsonView(IssueView.Read.class)
  public Date getResolutiondate() {
    return resolutiondate;
  }

  public void setResolutiondate(Date resolutiondate) {
    this.resolutiondate = resolutiondate;
  }

  public IssueResolution getResolution() {
    return resolution;
  }

  public void setResolution(IssueResolution resolution) {
    this.resolution = resolution;
  }

  public JiraUser getReporter() {
    return reporter;
  }

  public void setReporter(JiraUser reporter) {
    this.reporter = reporter;
  }

  @JsonView(IssueView.Update.class)
  public List<String> getLabels() {
    return labels;
  }

  public void setLabels(List<String> labels) {
    this.labels = labels;
  }

  @JsonView(IssueView.Read.class)
  public IssueStatus getStatus() {
    return status;
  }

  public void setStatus(IssueStatus status) {
    this.status = status;
  }

  public JiraUser getAssignee() {
    return assignee;
  }

  public void setAssignee(JiraUser assignee) {
    this.assignee = assignee;
  }

  @XmlElement(name = "customfield_10005")
  public int getStoryPoints() {
    return storyPoints;
  }

  public void setStoryPoints(int storyPoints) {
    this.storyPoints = storyPoints;
  }

  @XmlJavaTypeAdapter(JiraDateStringAdapter.class)
  @XmlElement(name = "customfield_10100")
  public Date getRtcCreated() {
    return rtcCreated;
  }

  public void setRtcCreated(Date rtcCreated) {
    this.rtcCreated = rtcCreated;
  }

  @XmlElement(name = "customfield_10101")
  public String getAcceptanceCriteria() {
    return acceptanceCriteria;
  }

  public void setAcceptanceCriteria(String acceptanceCriteria) {
    this.acceptanceCriteria = acceptanceCriteria;
  }

  @XmlElement(name = "customfield_10103")
  public JiraUser getOwner() {
    return owner;
  }

  public void setOwner(JiraUser owner) {
    this.owner = owner;
  }

  @XmlElement(name = "customfield_10104")
  public JiraUser getResolver() {
    return resolver;
  }

  public void setResolver(JiraUser resolver) {
    this.resolver = resolver;
  }



}
