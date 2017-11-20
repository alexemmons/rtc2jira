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
  L2("L2", "10303"),
  Services("Services", "10304"),
  LASD("LASD", "10305"),
  ARJIS("ARJIS (San Diego)", "10315"),
  Austin("Austin", "10316"),
  Bakersfield("Bakersfield (KernReDEx)", "10317"),
  Benton("Benton County", "10318"),
  CharlestonSO("Charleston SO", "10319"),
  CharlestonPD("Charleston PD", "10320"),
  Chattanooga("Chattanooga", "10321"),
  Colorado("Colorado", "10322"),
  ElPaso("El Paso", "10323"),
  Fresno("Fresno", "10324"),
  Houston("Houston", "10325"),
  HorryCounty("Horry County (South Carolina)", "10326"),
  Idaho("Idaho", "10327"),
  Illinois("Illinois (Chicago)", "10328"),
  Indianapolis("Indianapolis", "10329"),
  JPSO("JPSO Jefferson Parish", "10330"),
  KCETAC("KCETAC (Kansas City)", "10331"),
  LasVegas("Las Vegas Metro PD", "10332"),
  Maricopa("Maricopa", "10333"),
  Massachusetts("Massachusetts State Police", "10334"),
  Mesa("Mesa", "10335"),
  MiamiDade("Miami-Dade", "10336"),
  Modesto("Modesto", "10337"),
  NCTFC("NCTFC (North Central Texas Fusion - Collin County)", "10338"),
  OrangeCounty("Orange County", "10339"),
  Phoenix("Phoenix", "10340"),
  Polk("Polk County Iowa", "10341"),
  RAIN("RAIN", "10342"),
  Sacramento("Sacramento", "10343"),
  SanAntonio("San Antonio", "10344"),
  SanBernardino("San Bernardino", "10345"),
  SantaBarbara("Santa Barbara", "10346"),
  SavannahRiver("Savannah River", "10347"),
  SFBaySouth("SFBaySouth", "10348"),
  SFBayWest("SFBayWest", "10349"),
  Spokane("Spokane", "10350"),
  TBSN("TBSN (Tampa)", "10351"),
  Tucson("Tucson", "10352");

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
