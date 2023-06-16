package com.scd.gitlabtimeback.mapper;

import org.springframework.stereotype.Component;

import com.scd.gitlabtimeback.dto.UserDto;
import com.scd.gitlabtimeback.entity.User;

@Component
public class UserMapper {

    public UserDto mapUserToUserDto(User user) {
        return new UserDto(user.getId(), user.getName(),user.getState());
    }
}
