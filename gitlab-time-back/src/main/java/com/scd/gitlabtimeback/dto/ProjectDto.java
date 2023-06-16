package com.scd.gitlabtimeback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProjectDto {
    private Long id;
    private String name;
    private boolean archived;
}
