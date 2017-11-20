package to.rtc.rtc2jira.importer.mapping;

import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.ILiteral;
import com.ibm.team.workitem.common.model.Identifier;
import com.orientechnologies.orient.core.record.impl.ODocument;

import to.rtc.rtc2jira.importer.mapping.spi.MappingAdapter;
import to.rtc.rtc2jira.storage.FieldNames;

public class ImpactMapping extends MappingAdapter {

  private String value;

  @Override
  public void beforeWorkItem() {
    value = null;
  }

  @Override
  public void acceptAttribute(IAttribute attribute) {
	  Identifier<ILiteral> identifier = getValue(attribute);
	  value = identifier.getStringIdentifier();
  }

  @Override
  public void afterWorkItem(ODocument doc) {
    if (value != null) {
      doc.field(FieldNames.IMPACT, value);
    }
  }

}
