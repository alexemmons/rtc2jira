package to.rtc.rtc2jira.importer.mapping;

import com.ibm.team.workitem.common.model.IAttribute;
import com.orientechnologies.orient.core.record.impl.ODocument;

import to.rtc.rtc2jira.importer.mapping.spi.MappingAdapter;


public class LongMapping extends MappingAdapter {

  private Long value;
  private String identifier;

  public LongMapping(String identifier) {
    this.identifier = identifier;
  }

  @Override
  public void beforeWorkItem() {
    value = null;
  }

  @Override
  public void acceptAttribute(IAttribute attribute) {
    value = getValue(attribute);
  }

  @Override
  public void afterWorkItem(ODocument doc) {
    if (value != null) {
      doc.field(identifier, value);
    }
  }

}
