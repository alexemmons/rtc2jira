package to.rtc.rtc2jira.exporter.jira.mapping;

import to.rtc.rtc2jira.exporter.jira.entities.Issue;
import to.rtc.rtc2jira.storage.StorageEngine;

public class ReleaseIntroducedMapping implements Mapping {

	@Override
	public void map(Object value, Issue issue, StorageEngine storage) {
		if (value == null) {
			issue.getFields().setReleaseIntroduced(null);
		} else {
			String releaseIntroduced = value.toString();
			issue.getFields().setReleaseIntroduced(releaseIntroduced);
		}
	}

}
