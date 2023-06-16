package com.scd.gitlabtimeback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import com.scd.gitlabtimeback.enums.GroupingByField;
import com.scd.gitlabtimeback.enums.Period;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchData {
    private Long userId;
    private Long projectId;
    private Long issueId;
    private Timestamp from;
    private Timestamp to;
    private Period period;
    private boolean useSpentTime;


    public SearchData newWithChange(GroupingByField grouping, Long id) {
        SearchData ret = new SearchData(userId, projectId, issueId, from, to, period, useSpentTime);
  
        switch (grouping) {
            case ISSUE -> ret.setIssueId(id);
            case PERSON -> ret.setUserId(id);
            case PROJECT -> ret.setProjectId(id);
            case MONTH, MONTH_SPENT -> {
                ret.setPeriod(Period.CUSTOM);
                var date = YearMonth.parse(id.toString(), DateTimeFormatter.ofPattern("yyyyMM"));
                ret.setFrom(Timestamp.valueOf(date.atDay(1).toString().concat(" 00:00:00.00")));
                ret.setTo(Timestamp.valueOf(date.plusMonths(1).atDay(1).toString().concat(" 00:00:00.00")));
            }
        }

        return ret;
    }


}
