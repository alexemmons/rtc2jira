package to.rtc.rtc2jira.exporter.jira.entities;

public enum SeverityEnum {
  blocker("severity.literal.l6", "10306"), critical("severity.literal.l5", "10307"), major(
      "severity.literal.l4", "10308"), normal("severity.literal.l3", "10309"),
  		unclassified("severity.literal.l1", "10310");



  final private String rtcId;
  final private String jiraId;

  SeverityEnum(String rtcId, String jiraId) {
    this.rtcId = rtcId;
    this.jiraId = jiraId;
  }

  public String getRtcId() {
    return rtcId;
  }

  public String getJiraId() {
    return jiraId;
  }

  static public SeverityEnum fromRtcLiteral(String literal) {
    if (SeverityEnum.blocker.getRtcId().equals(literal)) {
      return SeverityEnum.blocker;
    } else if (SeverityEnum.critical.getRtcId().equals(literal)) {
      return SeverityEnum.critical;
    } else if (SeverityEnum.major.getRtcId().equals(literal)) {
      return SeverityEnum.major;
    } else if (SeverityEnum.normal.getRtcId().equals(literal)) {
      return SeverityEnum.normal;
//    } else if (SeverityEnum.minor.getRtcId().equals(literal)) {
//      return SeverityEnum.minor;
    } else if (SeverityEnum.unclassified.getRtcId().equals(literal)) {
      return SeverityEnum.unclassified;
    } else {
      throw new IllegalArgumentException("No corresponding SeverityValue for the literal "
          + literal);
    }
  }
  
  public CustomFieldOption getCustomFieldOption() {
    return new CustomFieldOption(getJiraId());
  }

}
