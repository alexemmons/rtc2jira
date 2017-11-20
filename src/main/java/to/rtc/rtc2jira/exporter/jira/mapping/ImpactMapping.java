package to.rtc.rtc2jira.exporter.jira.mapping;

import to.rtc.rtc2jira.exporter.jira.entities.ImpactEnum;
import to.rtc.rtc2jira.exporter.jira.entities.Issue;
import to.rtc.rtc2jira.storage.StorageEngine;

public class ImpactMapping implements Mapping {

	@Override
	public void map(Object value, Issue issue, StorageEngine storage) {
      try {
          String valueStr = (String) value;
          ImpactEnum impactEnum = ImpactEnum.fromRtcLiteral(valueStr);
          issue.getFields().setImpact(impactEnum.getCustomFieldOption());
        } catch (IllegalArgumentException e) {
          // leave priority unset. will be handled with PriorityMapper
        }
	}

}
