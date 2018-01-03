package to.rtc.rtc2jira.importer.mapping;

import java.util.logging.Logger;

import com.ibm.team.workitem.common.internal.model.Deliverable;
import com.ibm.team.workitem.common.internal.model.DeliverableHandle;
import com.ibm.team.workitem.common.model.IAttribute;
import com.orientechnologies.orient.core.record.impl.ODocument;

import to.rtc.rtc2jira.importer.RTCImporter;
import to.rtc.rtc2jira.importer.mapping.TargetMapping.IterationInfo;
import to.rtc.rtc2jira.importer.mapping.TargetMapping.RtcIterationType;
import to.rtc.rtc2jira.importer.mapping.spi.MappingAdapter;

public class DeliverableMapping extends MappingAdapter {
	  static final Logger LOGGER = Logger.getLogger(DeliverableMapping.class.getName());
	  static {
	    LOGGER.addHandler(RTCImporter.DEFAULT_LOG_HANDLER);
	  }

	private Deliverable value;
	private IterationInfo iterationInfo;
	private String identifier;
	private String name;
	
	public static final String NO_ITERATION = "NO_ITERATION";
	
	public DeliverableMapping(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public void beforeWorkItem() {
		value = null;
		iterationInfo = null;
	}

	@Override
	public void acceptAttribute(IAttribute attribute) {
		DeliverableHandle handle = getValue(attribute);
		if (handle != null) {
			Deliverable deliverable = fetchCompleteItem(handle);
			// might have to bail and make value a string: deliverable.getName();
//			value = deliverable;
			iterationInfo = fromRtcDeliverable(deliverable);
			name = deliverable.getName();
		}
	}

	@Override
	public void afterWorkItem(ODocument doc) {
//		if (value != null) {
//			doc.field(identifier, value);
//		}
	    if (iterationInfo != null) {
	        String iterationJson = iterationInfo.marshall();
//	        doc.field(identifier, iterationJson);
	        doc.field(identifier, name);
	      } else {
//	        doc.field(identifier, NO_ITERATION);
	      }
	}
	
	IterationInfo fromRtcDeliverable(Deliverable deliverable) {
		IterationInfo iterationInfo = new IterationInfo();
		iterationInfo.rtcId = deliverable.getItemId().toString();
	    iterationInfo.name = deliverable.getName();
//	    iterationInfo.label = iteration.getLabel();
//	    iterationInfo.startDate = iteration.getStartDate();
//	    iterationInfo.endDate = iteration.getEndDate();
	    iterationInfo.hasDeliverable = true;
	    iterationInfo.archived = deliverable.isArchived();
//	    if (iteration.getParent() != null) {
//	      iterationInfo.parent = fromRtcIteration(fetchCompleteItem(iteration.getParent()));
//	    }
	    iterationInfo.iterationType = RtcIterationType.release;
	      
		return iterationInfo;
	}

}
