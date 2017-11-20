package to.rtc.rtc2jira.exporter.jira.mapping;

import to.rtc.rtc2jira.exporter.jira.entities.Issue;
import to.rtc.rtc2jira.storage.StorageEngine;

public class APARMapping implements Mapping {

	@Override
	public void map(Object value, Issue issue, StorageEngine storage) {
	  if (value == null) {
	    issue.getFields().setAparId(null);
	  } else {
	    String aparId = value.toString();
	    issue.getFields().setAparId(aparId);
	  }
	}

}
