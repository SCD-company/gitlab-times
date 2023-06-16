package com.scd.gitlabtimeback.dto;

import lombok.Getter;

import javax.annotation.Nullable;

import com.scd.gitlabtimeback.enums.GroupingByField;
import com.scd.gitlabtimeback.enums.Period;

import static com.scd.gitlabtimeback.enums.GroupingByField.MONTH;
import static com.scd.gitlabtimeback.enums.GroupingByField.MONTH_SPENT;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class ReportParametersRequestDto {
    @Nullable
    private Long userId;
    @Nullable
    private Long projectId;
    @Nullable
    private Long issueId;
    @Nullable
    private DateBoundaryDto dateFrom;
    @Nullable
    private DateBoundaryDto dateTo;
    @Nullable
    private List<GroupingByField> grouping;
    @Nullable
    private Period period;
    @Nullable
    private Boolean useSpentTime;

    public ReportParametersRequestDto applyUseSpentTimeToGrouping() {

        if (Boolean.TRUE.equals(useSpentTime)) {

            grouping = Optional.ofNullable(grouping).map(gr -> {
                return gr.stream().map(g -> g == MONTH ? MONTH_SPENT : g).collect(Collectors.toList());
            }).orElse(grouping);

        }
        return this;
    }
}
