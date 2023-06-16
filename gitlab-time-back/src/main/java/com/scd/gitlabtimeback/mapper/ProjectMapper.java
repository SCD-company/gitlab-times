package com.scd.gitlabtimeback.mapper;

import org.springframework.stereotype.Component;

import com.scd.gitlabtimeback.dto.ProjectDto;
import com.scd.gitlabtimeback.entity.Project;

@Component
public class ProjectMapper {

    public ProjectDto mapProjectToProjectDto(Project project) {
        return new ProjectDto(project.getId(), project.getName(),project.isArchived());
    }
}
