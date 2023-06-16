package com.scd.gitlabtimeback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.scd.gitlabtimeback.api.TimeLogApi;
import com.scd.gitlabtimeback.dto.*;
import com.scd.gitlabtimeback.exception.BadRequestException;
import com.scd.gitlabtimeback.mapper.DateBoundaryMapper;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class TimeLogController {

    private final TimeLogApi timeLogApi;
    private final DateBoundaryMapper dateBoundaryMapper;

    public TimeLogController(@Autowired TimeLogApi timeLogApi,DateBoundaryMapper dateBoundaryMapper) {
        this.timeLogApi = timeLogApi;
        this.dateBoundaryMapper = dateBoundaryMapper;
    }

    @PostMapping(path = "report_get")
    public List<GroupingReportDto> getReportWithGrouping(@RequestBody ReportParametersRequestDto reportParametersRequestDto)
            throws BadRequestException {

        return timeLogApi.getTimeLogs(
                reportParametersRequestDto.applyUseSpentTimeToGrouping().getGrouping(),
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

    @PostMapping(path = "report_download")
    public DataFileDto getCsvReport(
            @RequestBody ReportParametersRequestDto reportParametersRequestDto
    ) throws BadRequestException {

        return timeLogApi.getCsvReport(
                reportParametersRequestDto.applyUseSpentTimeToGrouping().getGrouping(),
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

    @PostMapping(path = "report_download_pdf")
    public DataFileDto getPdfReport(
            @RequestBody ReportParametersRequestDto reportParametersRequestDto
    ) throws BadRequestException {

        return timeLogApi.getPdfReport(
                reportParametersRequestDto.applyUseSpentTimeToGrouping().getGrouping(),
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

