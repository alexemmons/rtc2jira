package to.rtc.rtc2jira.importer.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.orientechnologies.orient.core.record.impl.ODocument;

import to.rtc.rtc2jira.importer.mapping.spi.Mapping;
import to.rtc.rtc2jira.importer.mapping.spi.MappingRegistry;
import to.rtc.rtc2jira.storage.FieldNames;

public class DefaultMappingRegistry implements MappingRegistry {
  private final static DefaultMappingRegistry INSTANCE = new DefaultMappingRegistry();

  private Map<String, Mapping> mappings = new HashMap<>();
  private Mapping missingMapping = new MissingMapping();

  private DefaultMappingRegistry() {
    register(RTCIdentifierConstants.ID, new NullMapping());
    register(RTCIdentifierConstants.SUMMARY, new StringMapping(FieldNames.SUMMARY));
    register(RTCIdentifierConstants.DESCRIPTION, new StringMapping(FieldNames.DESCRIPTION));
    register(RTCIdentifierConstants.WORK_ITEM_TYPE, new StringMapping(FieldNames.WORK_ITEM_TYPE));
    register(RTCIdentifierConstants.ACCEPTANCE_CRITERIAS, new StringMapping(FieldNames.ACCEPTANCE_CRITERIAS));
    register(RTCIdentifierConstants.PTF, new StringMapping(FieldNames.PTF));
    register(RTCIdentifierConstants.MODIFIED, new TimestampMapping(FieldNames.MODIFIED));
    register(RTCIdentifierConstants.CREATIONDATE, new TimestampMapping(FieldNames.CREATIONDATE));
    register(RTCIdentifierConstants.COMMENTS, new CommentMapping());
    register(RTCIdentifierConstants.PRIORITY, new PriorityMapping());
    register(RTCIdentifierConstants.SEVERITY, new SeverityMapping());
    register(RTCIdentifierConstants.OWNER, new ContributorMapping(FieldNames.OWNER));
    register(RTCIdentifierConstants.CREATOR, new ContributorMapping(FieldNames.CREATOR));
    register(RTCIdentifierConstants.MODIFIED_BY, new ContributorMapping(FieldNames.MODIFIED_BY));
    register(RTCIdentifierConstants.RESOLVER, new ContributorMapping(FieldNames.RESOLVER));
    register(RTCIdentifierConstants.DURATION, new LongMapping(FieldNames.ESTIMATED_TIME));
    register(RTCIdentifierConstants.CORRECTED_ESTIMATE, new NullMapping());
    register(RTCIdentifierConstants.TIME_SPENT, new LongMapping(FieldNames.TIME_SPENT));
    register(RTCIdentifierConstants.CATEGORY, new CategoryMapping());
    register(RTCIdentifierConstants.ARCHIVED, new BooleanMapping(FieldNames.ARCHIVED));
    register(RTCIdentifierConstants.CONTEXT_ID, new NullMapping());
    register(RTCIdentifierConstants.PROJECT_AREA, new ProjectAreaMapping());
    register(RTCIdentifierConstants.SEQUENCE_VALUE, new NullMapping());
    register(RTCIdentifierConstants.TAGS, new TagsMapping());
    register(RTCIdentifierConstants.STORY_POINTS, new StoryPointsMapping());
    register(RTCIdentifierConstants.CUSTOM_ATTRIBUTES, new CustomAttributeMapping());
    register(RTCIdentifierConstants.APPROVALS, new ApprovalMapping());
    register(RTCIdentifierConstants.APPROVAL_DESCRIPTORS, new ApprovalDescriptorMapping());
    register(RTCIdentifierConstants.RESOLUTION, new ResolutionMapping());
    register(RTCIdentifierConstants.RESOLUTION_DATE, new DateMapping(FieldNames.RESOLUTION_DATE));
    register(RTCIdentifierConstants.STATE, new StateMapping());
    register(RTCIdentifierConstants.TARGET, new TargetMapping());
    register(RTCIdentifierConstants.DUE_DATE, new DateMapping(FieldNames.DUE_DATE));
    register(RTCIdentifierConstants.SUBSCRIPTIONS, new SubscriptionsMapping());
    register(RTCIdentifierConstants.STATE_TRANSITIONS, new NullMapping());
//    register(RTCIdentifierConstants.ESTIMATED_HOURS, new IntegerMapping(FieldNames.ESTIMATED_TIME));
    register(RTCIdentifierConstants.PROJECT_NAME, new BisonProjectMapping());
    register(RTCIdentifierConstants.PROJECT_NAME_DEPRECATED, new NullMapping());
    register(RTCIdentifierConstants.REFERENCE_MODEL, new RefModelMapping());
    register(RTCIdentifierConstants.MAXIMAL_ESTIMATE, new NullMapping());
    register(RTCIdentifierConstants.MINIMAL_ESTIMATE, new NullMapping());
    register(RTCIdentifierConstants.NEW_RANKING, new NullMapping());
    register(RTCIdentifierConstants.RANK, new NullMapping());
    register(RTCIdentifierConstants.RELATED_PROJECTS, new RelatedProjectsMapping());
    register(RTCIdentifierConstants.INDUSTRY_SECTOR, new IndustrySectorMapping());
    register(RTCIdentifierConstants.EP_REQ_NR, new IntegerMapping(FieldNames.EP_REQ_NR));
    register(RTCIdentifierConstants.EP_REQ_NR_UG, new IntegerMapping(FieldNames.EP_REQ_NR_UG));
    register(RTCIdentifierConstants.DP_REQ_NR, new IntegerMapping(FieldNames.DP_REQ_NR));
    register(RTCIdentifierConstants.PO_PREPLANNING_DONE, new BooleanMapping(FieldNames.PO_PREPLANNING_DONE));
    register(RTCIdentifierConstants.DEVL_DEADLINE, new DevlDeadlineMapping());
    register(RTCIdentifierConstants.SECOND_ESTIMATION, new IntegerMapping(FieldNames.SECOND_ESTIMATION));
    register(RTCIdentifierConstants.BUDGET, new IntegerMapping(FieldNames.MARKET_BUDGET));
    register(RTCIdentifierConstants.TEST_SCRIPT_IDS, new StringMapping(FieldNames.TEST_SCRIPT_IDs));
    register(RTCIdentifierConstants.SILO_RANKING, new SiloRankingMapping());
    register(RTCIdentifierConstants.SILO_RANKING_OLD, new NullMapping());
    register(RTCIdentifierConstants.COMPETENCE_CENTER, new CompetenceCenterMapping());
    register(RTCIdentifierConstants.SEQUENCE_CREATION_BN, new NullMapping());
    register(RTCIdentifierConstants.TIMESLOT, new TimeSlotMapping());
    register(RTCIdentifierConstants.PRODUCT_OWNER, new ContributorMapping(FieldNames.PRODUCT_OWNER));
    register(RTCIdentifierConstants.TARGET_RELEASE, new NullMapping());
    register(RTCIdentifierConstants.CLASSIFICATION_TRADING_FIRM, new NullMapping());
    register(RTCIdentifierConstants.TEST_COVERAGE, new NullMapping());
    register(RTCIdentifierConstants.STAKEHOLDER, new NullMapping());
    register(RTCIdentifierConstants.BUSINESS_VALUE_CHF, new NullMapping());
    register(RTCIdentifierConstants.SUPERIOR_TARGET, new NullMapping());
    register(RTCIdentifierConstants.ROADMAP, new BooleanMapping(FieldNames.ROADMAP));
    register(RTCIdentifierConstants.CUSTOMER, new CustomerMapping());
    register(RTCIdentifierConstants.REALISED_BY_IDM, new RealisedByIdmMapping());
    register(RTCIdentifierConstants.IMPLEMENTATION_LEVEL, new ImplementationLevelMapping());
    register(RTCIdentifierConstants.IMPACT, new ImpactMapping());
    register(RTCIdentifierConstants.AFFECTED_TEAMS, new NullMapping());
    register(RTCIdentifierConstants.APAR, new StringMapping(FieldNames.APAR));
    register(RTCIdentifierConstants.CUSTOMER_LIST, new StringListMapping(FieldNames.CUSTOMER_LIST));
    register(RTCIdentifierConstants.FIXED_IN, new DeliverableMapping(FieldNames.FIXED_IN));
    register(RTCIdentifierConstants.FOUND_IN, new DeliverableMapping(FieldNames.FOUND_IN));
    register(RTCIdentifierConstants.RELEASE_INTRODUCED, new DeliverableMapping(FieldNames.RELEASE_INTRODUCED));
    
  };

  public static DefaultMappingRegistry getInstance() {
    return INSTANCE;
  }

  @Override
  public void register(String rtcIdentifier, Mapping mapping) {
    mappings.put(rtcIdentifier, mapping);
  }

  private Mapping getMapping(String rtcIdentifier) {
    return Optional.ofNullable(mappings.get(rtcIdentifier)).orElse(missingMapping);
  }

  public void beforeWorkItem(final IWorkItem workItem) {
    missingMapping.beforeWorkItem(workItem);
    mappings.values().forEach(m -> {
      m.beforeWorkItem(workItem);
    });
  }

  public void acceptAttribute(IAttribute attribute) {
    String identifier = attribute.getIdentifier();
    getMapping(identifier).acceptAttribute(attribute);
  }

  public void afterWorkItem(final ODocument doc) {
    missingMapping.afterWorkItem(doc);
    mappings.values().forEach(m -> {
      m.afterWorkItem(doc);
    });
  }

}
