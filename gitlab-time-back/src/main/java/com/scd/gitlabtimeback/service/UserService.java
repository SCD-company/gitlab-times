package com.scd.gitlabtimeback.service;


import com.scd.gitlabtimeback.dto.UserDto;
import com.scd.gitlabtimeback.entity.User;
import com.scd.gitlabtimeback.mapper.UserMapper;
import com.scd.gitlabtimeback.repository.dsl.UserDslRepository;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserDslRepository userDslRepository;
    private final UserMapper userMapper;

    public UserService(@Autowired UserDslRepository userDslRepository, @Autowired UserMapper userMapper) {
        this.userDslRepository = userDslRepository;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userDslRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAllActiveByProjectId(Long currentUserId,Long projectId){
        return userDslRepository.findAllActiveByProjectId(currentUserId,projectId)
                .stream()
                .map(userMapper::mapUserToUserDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Boolean userIsAdmin(@NotNull Long id) {

        if (userDslRepository.isUserExists(id)) {
            return userDslRepository.findById(id).getAdmin();
        }

        return false;
    }
}
