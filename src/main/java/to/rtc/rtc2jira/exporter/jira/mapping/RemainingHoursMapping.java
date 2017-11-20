/*
 * Copyright (c) 2015 BISON Schweiz AG, All Rights Reserved.
 */
package to.rtc.rtc2jira.exporter.jira.mapping;

import java.util.EnumSet;

import to.rtc.rtc2jira.exporter.jira.entities.Issue;
import to.rtc.rtc2jira.exporter.jira.entities.ResolutionEnum;
import to.rtc.rtc2jira.exporter.jira.entities.Timetracking;
import to.rtc.rtc2jira.storage.StorageEngine;


public class RemainingHoursMapping implements Mapping {

  static final EnumSet<ResolutionEnum> NO_REMAINING_TIME = EnumSet.of(ResolutionEnum.cannotReproduce,
      ResolutionEnum.done, ResolutionEnum.duplicate, ResolutionEnum.fixed, ResolutionEnum.wont_fix,
      ResolutionEnum.wontDo);

  @Override
  public void map(Object value, Issue issue, StorageEngine storage) {
	Timetracking timetracking = issue.getFields().getTimetracking();
    if (value == null) {
      timetracking.setRemainingEstimate("0m");
    } else {
      Long millis = (Long) value;
      Long hours = millis / 3600000;
      if (hours <= 0) {
        timetracking.setRemainingEstimate("0m");
      } else {
        int minutes = hours.intValue() * 60;
        timetracking.setRemainingEstimate(minutes + "m");
//        IssueResolution resolution = issue.getFields().getResolution();
//        if (resolution != null && NO_REMAINING_TIME.contains(resolution.getEnum())) {
//          timetracking.setRemainingEstimate("0m");
//        }
      }
      issue.getFields().setTimetracking(timetracking);
    }
  }
}
