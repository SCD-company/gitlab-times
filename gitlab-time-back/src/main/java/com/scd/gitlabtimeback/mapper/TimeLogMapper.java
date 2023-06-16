package com.scd.gitlabtimeback.mapper;

import com.querydsl.core.Tuple;
import com.scd.gitlabtimeback.dto.GroupingReportDto;
import com.scd.gitlabtimeback.dto.ProjectDto;
import com.scd.gitlabtimeback.dto.TimeLogDto;
import com.scd.gitlabtimeback.dto.UserDto;
import com.scd.gitlabtimeback.entity.TimeLog;
import com.scd.gitlabtimeback.enums.GroupingByField;

import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Component
public class TimeLogMapper {

        public GroupingReportDto mapTupleToGroupingReportDto(Tuple tuple, GroupingByField grouping) {

                Double time = tuple.get(2, Double.class);

                if (time != null) {
                        time = time / 3600;
                }
                else {
                       time = 0.0;
                }

                Boolean archived = tuple.get(3,Boolean.class);

                return new GroupingReportDto(
                                grouping,
                                tuple.get(0, Long.class),
                                (grouping == GroupingByField.MONTH || grouping == GroupingByField.MONTH_SPENT)
                                                ? YearMonth.parse(tuple.get(1, String.class),
                                                                DateTimeFormatter.ofPattern("yyyyMM"))
                                                                .format(DateTimeFormatter.ofPattern("MM/yyyy"))
                                                : tuple.get(1, String.class),
                                time,
                                !(archived==null? false:archived),
                                null);
        }

        public TimeLogDto mapTimeLogToTimeLogDto(TimeLog timeLog) {
                return new TimeLogDto(timeLog.getId(),
                                timeLog.getTimeSpent(),
                                new UserDto(timeLog.getUser().getId(), timeLog.getUser().getName(),timeLog.getUser().getState()),
                                timeLog.getIssue() != null ? timeLog.getIssue().getTitle() : "",
                                new ProjectDto(timeLog.getProject().getId(), timeLog.getProject().getName(),timeLog.getProject().isArchived()),
                                timeLog.getCreatedAt());
        }

}
