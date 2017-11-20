package to.rtc.rtc2jira.exporter.jira.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import to.rtc.rtc2jira.ExportManager;
import to.rtc.rtc2jira.exporter.jira.entities.CustomFieldOption;
import to.rtc.rtc2jira.exporter.jira.entities.CustomerListEnum;
import to.rtc.rtc2jira.exporter.jira.entities.Issue;
import to.rtc.rtc2jira.storage.StorageEngine;


public class CustomerListMapping implements Mapping {
  private static final Logger LOGGER = Logger.getLogger(CustomerListMapping.class.getName());
  static {
    LOGGER.addHandler(ExportManager.DEFAULT_LOG_HANDLER);
  }

  @Override
  public void map(Object value, Issue issue, StorageEngine storage) {
	if (value != null) {
	    List<String> customerList = (List<String>) value;
		for (String customer : customerList) {
	//			CustomerListEnum.forRtcId(customer)
	//					.ifPresent((customerEnum) -> issue.getFields().getCustomer15().add(customerEnum.getCustomFieldOption()));
			
			Optional<CustomerListEnum> customerEnum = CustomerListEnum.forRtcId(customer);
			if (customerEnum.isPresent()) {
				CustomFieldOption option = customerEnum.get().getCustomFieldOption();
				List<CustomFieldOption> list = issue.getFields().getCustomer15();
				list.add(option);
				issue.getFields().setCustomer15(list);
			}
		}
	}
  }
}
