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
import java.util.ArrayList;
import java.util.List;

@Component
public class TimeLogMapper {

        public GroupingReportDto mapTupleToGroupingReportDto(Tuple tuple, GroupingByField grouping) {

                Double time = tuple.get(2, Double.class);

                if (time != null) {
                        time = time / 3600;
                } else {
                        time = 0.0;
                }

                Boolean archived = tuple.get(3, Boolean.class);

                List<String> names;
                var value = tuple.get(1, String.class);

                switch (grouping) {
                        case MONTH:
                        case MONTH_SPENT:
                                names = List.of(YearMonth.parse(value,
                                                DateTimeFormatter
                                                                .ofPattern("yyyyMM"))
                                                .format(DateTimeFormatter
                                                                .ofPattern("MM/yyyy")));
                                break;
                        case ISSUE:
                                names = formatIssue(value);
                                break;
                        default:
                                names = List.of(value);
                                break;

                }

                return new GroupingReportDto(
                                grouping,
                                tuple.get(0, Long.class),
                                names,
                                time,
                                !(archived == null ? false : archived),
                                null);
        }

        private List<String> formatIssue(String issue) {

                List<String> ret = new ArrayList<>(3);

                String parts[] = issue.split(" ");

                String closedAt = parts[1].equals("false") ? "[open]"
                                : "[" + YearMonth.parse(parts[1], DateTimeFormatter
                                                .ofPattern("yyyyMM"))
                                                .format(DateTimeFormatter
                                                                .ofPattern("MM/yyyy"))
                                                + "]";

              

                ret.add("#"+parts[0]);
                ret.add(closedAt);

                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < parts.length; i++) {
                        sb.append(" ").append(parts[i]);
                }

                ret.add(sb.toString());

                return ret;

        }

        public TimeLogDto mapTimeLogToTimeLogDto(TimeLog timeLog) {
                return new TimeLogDto(timeLog.getId(),
                                timeLog.getTimeSpent(),
                                new UserDto(timeLog.getUser().getId(), timeLog.getUser().getName(),
                                                timeLog.getUser().getState()),
                                timeLog.getIssue() != null ? timeLog.getIssue().getTitle() : "",
                                new ProjectDto(timeLog.getProject().getId(), timeLog.getProject().getName(),
                                                timeLog.getProject().isArchived()),
                                timeLog.getCreatedAt());
        }

}
