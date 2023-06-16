package com.scd.gitlabtimeback.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.scd.gitlabtimeback.dto.*;
import com.scd.gitlabtimeback.enums.GroupingByField;
import com.scd.gitlabtimeback.enums.Period;
import com.scd.gitlabtimeback.exception.BadRequestException;
import com.scd.gitlabtimeback.fileFillers.CsvFiller;
import com.scd.gitlabtimeback.fileFillers.PdfFiller;
import com.scd.gitlabtimeback.service.TimeLogService;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Component
public class TimeLogApi {

    private final TimeLogService timeLogService;
    private final UserApi userApi;
    private final CsvFiller csvFiller;
    private final PdfFiller pdfFiller;

    public TimeLogApi(@Autowired TimeLogService timeLogService, @Autowired UserApi userApi,
                      @Autowired CsvFiller csvFiller, @Autowired PdfFiller pdfFiller) {
        this.timeLogService = timeLogService;
        this.userApi = userApi;
        this.csvFiller = csvFiller;
        this.pdfFiller = pdfFiller;
    }

    public DataFileDto getCsvReport(List<GroupingByField> grouping, SearchData data){

        return new DataFileDto(
                "timeLog" + (new Date()).getTime() + ".csv",
                csvFiller.fillFile(grouping, getTimeLogs(grouping, data))
        );
    }

    public DataFileDto getPdfReport(List<GroupingByField> grouping, SearchData data){

        return new DataFileDto(
                "timeLog" + (new Date()).getTime() + ".pdf",
                pdfFiller.fillFile(grouping, getTimeLogs(grouping, data))
        );
    }


    public List<GroupingReportDto> getTimeLogs(List<GroupingByField> grouping, SearchData data) {
        if (grouping.isEmpty()) {
            return Collections.emptyList();
        }

       

        var currentField = grouping.get(0);
        var result = getTimeLogsWithFiltersAndGrouping(data, currentField);

        result.forEach(resultEntry ->
                resultEntry.setSubGroup(
                        getTimeLogs(
                                grouping.subList(1, grouping.size()),
                                data.newWithChange(currentField, resultEntry.getId())
                        )
                )
        );
        return result;
    }


    public List<GroupingReportDto> getTimeLogsWithFiltersAndGrouping(SearchData data, GroupingByField grouping) throws BadRequestException {


        if (data.getPeriod() != Period.CUSTOM && data.getPeriod()!=null) {
            if (data.getTo() != null || data.getFrom() != null) {
                throw new BadRequestException("dates must not be specfied for period "+data.getPeriod());
            }
        }

        if (data.getFrom() != null && data.getTo() != null && data.getFrom().after(data.getTo())) {
            throw new BadRequestException("First date must be before second");
        }

        UserDetailsDto currentUser = userApi.getCurrentUser();

        return timeLogService.findAllWithFiltersAndGrouping(
                currentUser,
                data.getUserId(),
                data.getProjectId(),
                data.getIssueId(),
                getDateOfPeriod(data.getPeriod(), true,data.getFrom())  ,
                getDateOfPeriod(data.getPeriod(), false,data.getTo())  ,
                grouping,
                data.isUseSpentTime());

    }

    private Timestamp getDateOfPeriod(Period period, Boolean isFirst, Timestamp customDate) {

        if(period==null) {
            return null;
        }

        Calendar date = Calendar.getInstance();
        switch (period) {
            case CUR_WEEK -> {
                date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                date.add(Calendar.DAY_OF_YEAR, isFirst ? 0 : Calendar.DAY_OF_WEEK);
            }
            case LAST_WEEK -> {
                date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                date.add(Calendar.DAY_OF_YEAR, isFirst ? -Calendar.DAY_OF_WEEK : 0);
            }
            case CUR_MONTH -> {
                date.set(Calendar.DAY_OF_MONTH, 1);
                date.add(Calendar.MONTH, isFirst ? 0 : 1);
            }
            case LAST_MONTH -> {
                date.set(Calendar.DAY_OF_MONTH, 1);
                date.add(Calendar.MONTH, isFirst ? -1 : 0);
            }
            case CUSTOM -> {
                return customDate;
            }
        }

        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        return new Timestamp(date.getTimeInMillis());
    }

}
