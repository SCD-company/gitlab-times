package com.scd.gitlabtimeback.mapper;

import com.querydsl.core.Tuple;
import com.scd.gitlabtimeback.dto.GroupingReportDto;
import com.scd.gitlabtimeback.dto.ProjectDto;
import com.scd.gitlabtimeback.dto.TimeLogDto;
import com.scd.gitlabtimeback.dto.UserDto;
import com.scd.gitlabtimeback.entity.TimeLog;
import com.scd.gitlabtimeback.enums.GroupingByField;
import com.scd.gitlabtimeback.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;



@Component
@RequiredArgsConstructor
public class TimeLogMapper {

        final ProjectRepository projectRepository;

        @Value("${gitlab.issue.link.base}")
        private String baseURL;

        @Transactional(propagation = Propagation.MANDATORY)
        public GroupingReportDto mapTupleToGroupingReportDto(Tuple tuple, GroupingByField grouping) {

                Double time = tuple.get(2, Double.class);

                if (time != null) {
                        time = time / 3600;
                } else {
                        time = 0.0;
                }

                Boolean archived = tuple.get(3, Boolean.class);

                List<GroupingReportDto.Cell> names;
                var value = tuple.get(1, String.class);

                switch (grouping) {
                        case MONTH:
                        case MONTH_SPENT:
                                names = List.of(new GroupingReportDto.TextCell(YearMonth.parse(value,
                                                DateTimeFormatter
                                                                .ofPattern("yyyyMM"))
                                                .format(DateTimeFormatter
                                                                .ofPattern("MM/yyyy"))));
                                break;
                        case ISSUE:
                                names = formatIssue(value);
                                break;
                        default:
                                names = List.of(new GroupingReportDto.TextCell(value));
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

        private List<GroupingReportDto.Cell> formatIssue(String issue) {

                List<GroupingReportDto.Cell> ret = new ArrayList<>(3);

                String parts[] = issue.split(" ");

                String closedAt = parts[1].equals("false") ? "[open]"
                                : "[" + YearMonth.parse(parts[1], DateTimeFormatter
                                                .ofPattern("yyyyMM"))
                                                .format(DateTimeFormatter
                                                                .ofPattern("MM/yyyy"))
                                                + "]";

              

              

                long projectId = Long.parseLong(parts[2]);

                var project = projectRepository.findById(projectId).get();

                String path = baseURL+"/"+project.getNamespace().getPath()+"/"+project.getPath()+"/-/issues/"+parts[0];

                ret.add(new GroupingReportDto.LinkCell("#"+parts[0],path));
                ret.add(new GroupingReportDto.TextCell(closedAt));

                StringBuilder sb = new StringBuilder();
                for (int i = 3; i < parts.length; i++) {
                        sb.append(" ").append(parts[i]);
                }

                ret.add(new GroupingReportDto.TextCell(sb.toString()));

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
