package com.scd.gitlabtimeback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.scd.gitlabtimeback.api.OptionsApi;
import com.scd.gitlabtimeback.dto.OptionsDto;
import com.scd.gitlabtimeback.dto.ReportParametersRequestDto;
import com.scd.gitlabtimeback.dto.SearchData;
import com.scd.gitlabtimeback.exception.BadRequestException;
import com.scd.gitlabtimeback.mapper.DateBoundaryMapper;

@RestController
@RequestMapping(path = "/api/options")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class OptionsController {

    private final OptionsApi optionsApi;
    private final DateBoundaryMapper dateBoundaryMapper;

    public OptionsController(@Autowired OptionsApi optionsApi,DateBoundaryMapper dateBoundaryMapper) {
        this.optionsApi = optionsApi;
        this.dateBoundaryMapper = dateBoundaryMapper;
    }

    @PostMapping
    public OptionsDto getOptions(@RequestBody ReportParametersRequestDto reportParametersRequestDto)
            throws BadRequestException {

        return optionsApi.getOptions(
                new SearchData(
                        reportParametersRequestDto.getUserId(),
                        reportParametersRequestDto.getProjectId(),
                        reportParametersRequestDto.getIssueId(),
                        dateBoundaryMapper.getFrom(reportParametersRequestDto.getDateFrom()),
                        dateBoundaryMapper.getTo(reportParametersRequestDto.getDateTo()),
                        reportParametersRequestDto.getPeriod(),
                        Boolean.TRUE.equals(reportParametersRequestDto.getUseSpentTime())
                )
        );
    }
}
