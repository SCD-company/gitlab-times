package com.scd.gitlabtimeback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReportItemDto {
    private String userName;
    private String projectName;
    private String issueName;
    private Long spendTime;
}
