package to.rtc.rtc2jira.exporter.jira.entities;

public enum ImpactEnum {
	major("impact.literal.l4", "10314"),
	significant("impact.literal.l3", "10313"),
	moderate("impact.literal.l2", "10312"),
	minor("impact.literal.l1", "10311");



  final private String rtcId;
  final private String jiraId;

  ImpactEnum(String rtcId, String jiraId) {
    this.rtcId = rtcId;
    this.jiraId = jiraId;
  }

  public String getRtcId() {
    return rtcId;
  }

  public String getJiraId() {
    return jiraId;
  }

  static public ImpactEnum fromRtcLiteral(String literal) {
    if (ImpactEnum.minor.getRtcId().equals(literal)) {
      return ImpactEnum.minor;
    } else if (ImpactEnum.moderate.getRtcId().equals(literal)) {
      return ImpactEnum.moderate;
    } else if (ImpactEnum.significant.getRtcId().equals(literal)) {
      return ImpactEnum.significant;
    } else if (ImpactEnum.major.getRtcId().equals(literal)) {
      return ImpactEnum.major;
    } else {
      throw new IllegalArgumentException("No corresponding ImpactValue for the literal "
          + literal);
    }
  }
  
  public CustomFieldOption getCustomFieldOption() {
    return new CustomFieldOption(getJiraId());
  }

}
