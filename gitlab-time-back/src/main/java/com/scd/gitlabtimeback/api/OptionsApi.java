package com.scd.gitlabtimeback.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.scd.gitlabtimeback.dto.*;
import com.scd.gitlabtimeback.service.ProjectService;
import com.scd.gitlabtimeback.service.UserService;

import java.util.*;

@Component
public class OptionsApi {

    private final UserService userService;
    private final ProjectService projectService;
    private final UserApi userApi;

    public OptionsApi(@Autowired UserService userService,
            @Autowired ProjectService projectService, UserApi userApi) {
        this.userService = userService;
        this.projectService = projectService;
        this.userApi = userApi;
    }

    public OptionsDto getOptions(SearchData searchData) {

        UserDetailsDto currentUser = userApi.getCurrentUser();

        Long currentUserId = currentUser.getAdmin() ? null : currentUser.getId();

        List<UserDto> users = userService.findAllActiveByProjectId(currentUserId, searchData.getProjectId());

        List<ProjectDto> projects = projectService.findAllByUserId(currentUserId, searchData.getUserId());

        return new OptionsDto(users, projects);
    }
}
