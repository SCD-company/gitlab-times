package com.scd.gitlabtimeback.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scd.gitlabtimeback.dto.GroupingReportDto;
import com.scd.gitlabtimeback.dto.UserDetailsDto;
import com.scd.gitlabtimeback.enums.GroupingByField;
import com.scd.gitlabtimeback.mapper.TimeLogMapper;
import com.scd.gitlabtimeback.repository.dsl.TimeLogDslRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeLogService {

    private final TimeLogDslRepository timeLogDslRepository;
    private final TimeLogMapper timeLogMapper;


    public TimeLogService(@Autowired TimeLogDslRepository timeLogDslRepository, @Autowired TimeLogMapper timeLogMapper) {
        this.timeLogDslRepository = timeLogDslRepository;
        this.timeLogMapper = timeLogMapper;
    }


    @Transactional(readOnly = true)
    public List<GroupingReportDto> findAllWithFiltersAndGrouping(UserDetailsDto currentUser,
            Long user, Long project, Long issue, Timestamp from, Timestamp to, GroupingByField group, boolean useSpentTime) {
        return timeLogDslRepository.findAllWithFiltersAndGrouping(currentUser.getAdmin()?null:currentUser.getId(),user, project, issue, from, to, group, useSpentTime)
                .stream()
                .map(item -> timeLogMapper.mapTupleToGroupingReportDto(item, group))
                .collect(Collectors.toList());

    }
}
