package to.rtc.rtc2jira.exporter.jira.mapping;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import to.rtc.rtc2jira.ExportManager;
import to.rtc.rtc2jira.exporter.jira.entities.Issue;
import to.rtc.rtc2jira.exporter.jira.entities.IssueFields;
import to.rtc.rtc2jira.exporter.jira.entities.Version;
import to.rtc.rtc2jira.importer.mapping.TargetMapping;
import to.rtc.rtc2jira.importer.mapping.TargetMapping.IterationInfo;
import to.rtc.rtc2jira.storage.StorageEngine;

public class FixedInMapping implements Mapping {
	  static Logger LOGGER = Logger.getLogger(FixedInMapping.class.getName());
	  static {
	    LOGGER.addHandler(ExportManager.DEFAULT_LOG_HANDLER);
	  }

	@Override
	public void map(Object value, Issue issue, StorageEngine storage) {
	    IterationInfo iterInfo = new IterationInfo();
	    if (value != null && !TargetMapping.NO_ITERATION.equals(value)) {
	      try {
	        iterInfo.unmarshall((String) value);
	        IssueFields fields = issue.getFields();
	        IterationHandler iterationHandler = IterationHandler.INSTANCE;
//	        fields.setPlannedFor(iterationHandler.getIterationQName(iterInfo));
	        IterationInfo versionIterInfo = IterationMapping.getVersionIteration(iterInfo);
	        if (versionIterInfo != null) {
	          Version version = iterationHandler.getVersion(versionIterInfo, fields.getProject());
	          fields.setFixedIn(version);
	        }
	      } catch (IOException e) {
	        LOGGER.log(Level.SEVERE, "Could not unmarshall Iteration json: " + e.getMessage());
	      }
	    }
	}

}
