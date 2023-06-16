package com.scd.gitlabtimeback.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scd.gitlabtimeback.dto.ProjectDto;
import com.scd.gitlabtimeback.mapper.ProjectMapper;
import com.scd.gitlabtimeback.repository.dsl.ProjectDslRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectDslRepository projectDslRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(@Autowired ProjectDslRepository projectDslRepository, @Autowired ProjectMapper projectMapper) {
        this.projectDslRepository = projectDslRepository;
        this.projectMapper = projectMapper;
    }


    @Transactional(readOnly = true)
    public List<ProjectDto> findAllByUserId(Long currentUserId,Long userId){
        return projectDslRepository.findAllByUserId(currentUserId,userId)
                .stream()
                .map(projectMapper::mapProjectToProjectDto)
                .collect(Collectors.toList());
    }

}
