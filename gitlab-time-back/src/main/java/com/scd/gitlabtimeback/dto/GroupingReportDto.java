package com.scd.gitlabtimeback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.scd.gitlabtimeback.enums.GroupingByField;

@AllArgsConstructor
@Setter
@Getter
public class GroupingReportDto {
    private GroupingByField type;
    private Long id;
    private List<String> names;
    private Double time;
    private boolean actual;
    private List<GroupingReportDto> subGroup;

}
