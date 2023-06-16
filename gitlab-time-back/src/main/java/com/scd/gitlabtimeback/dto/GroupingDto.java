package com.scd.gitlabtimeback.dto;

import com.scd.gitlabtimeback.enums.GroupingByField;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GroupingDto {
    private GroupingByField grouping;
    private String value;
}
