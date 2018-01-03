package to.rtc.rtc2jira.exporter.jira.entities;

import java.util.EnumSet;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import to.rtc.rtc2jira.ExportManager;

public enum CustomerListEnum {

//  UNSET("cl1.literal.l28", "10302") {
//    @Override
//    public CustomFieldOption getCustomFieldOption() {
//      return CustomFieldOption.NONE;
//    }
//  }, //
//  NONE("cl1.literal.l28", "10302"),
  L2("L2", "10071"),
  Services("Services", "10072"),
  LASD("LASD", "10073"),
  ARJIS("ARJIS (San Diego)", "10074"),
  Austin("Austin", "10075"),
  Bakersfield("Bakersfield (KernReDEx)", "10076"),
  Benton("Benton County", "10077"),
  CharlestonSO("Charleston SO", "10078"),
  CharlestonPD("Charleston PD", "10079"),
  Chattanooga("Chattanooga", "10080"),
  Colorado("Colorado", "10081"),
  ElPaso("El Paso", "10082"),
  Fresno("Fresno", "10083"),
  Houston("Houston", "10084"),
  HorryCounty("Horry County (South Carolina)", "10085"),
  Idaho("Idaho", "10086"),
  Illinois("Illinois (Chicago)", "10087"),
  Indianapolis("Indianapolis", "10088"),
  JPSO("JPSO Jefferson Parish", "10089"),
  KCETAC("KCETAC (Kansas City)", "10090"),
  LasVegas("Las Vegas Metro PD", "10091"),
  Maricopa("Maricopa", "10092"),
  Massachusetts("Massachusetts State Police", "10093"),
  Mesa("Mesa", "10094"),
  MiamiDade("Miami-Dade", "10095"),
  Modesto("Modesto", "10096"),
  NCTFC("NCTFC (North Central Texas Fusion - Collin County)", "10097"),
  OrangeCounty("Orange County", "10098"),
  Phoenix("Phoenix", "10099"),
  Polk("Polk County Iowa", "10100"),
  RAIN("RAIN", "10101"),
  Sacramento("Sacramento", "10102"),
  SanAntonio("San Antonio", "10103"),
  SanBernardino("San Bernardino", "10104"),
  SantaBarbara("Santa Barbara", "10105"),
  SavannahRiver("Savannah River", "10106"),
  SFBaySouth("SFBaySouth", "10107"),
  SFBayWest("SFBayWest", "10108"),
  Spokane("Spokane", "10109"),
  TBSN("TBSN (Tampa)", "10110"),
  Tucson("Tucson", "10111");

  private String rctId;
  private String jiraId;
  static private final Logger LOGGER = Logger.getLogger(CustomerListEnum.class.getName());
  static {
    LOGGER.addHandler(ExportManager.DEFAULT_LOG_HANDLER);
  }

  private CustomerListEnum(String rctId, String jiraId) {
    this.rctId = rctId;
    this.jiraId = jiraId;
  }

  public String getRctId() {
    return rctId;
  }

  public void setRctId(String rctId) {
    this.rctId = rctId;
  }

  public String getJiraId() {
    return jiraId;
  }

  public void setJiraId(String jiraId) {
    this.jiraId = jiraId;
  }

  public CustomFieldOption getCustomFieldOption() {
    return new CustomFieldOption(getJiraId());
  }

  public static final Optional<CustomerListEnum> forJiraId(String jiraId) {
    EnumSet<CustomerListEnum> all = EnumSet.allOf(CustomerListEnum.class);
    Optional<CustomerListEnum> first = all.stream().filter(item -> item.getJiraId().equals(jiraId)).findFirst();
    if (!first.isPresent()) {
      LOGGER.log(Level.SEVERE, "Could not find a CustomerListEnum entry for the jira id " + jiraId);
    }
    return first;
  }

  public static final Optional<CustomerListEnum> forRtcId(String rtcId) {
    EnumSet<CustomerListEnum> all = EnumSet.allOf(CustomerListEnum.class);
    Optional<CustomerListEnum> first = all.stream().filter(item -> item.getRctId().equals(rtcId)).findFirst();
    if (!first.isPresent()) {
      LOGGER.log(Level.SEVERE, "Could not find a CustomerListEnum entry for the rtc id " + rtcId);
    }
    return first;
  }


}
