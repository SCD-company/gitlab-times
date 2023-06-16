package com.scd.gitlabtimeback.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class TimeLogDto {
    private Long id;
    private double timeSpent;
    private UserDto user;
    private String issue;
    private ProjectDto project;
    private Timestamp createdAt;
}
