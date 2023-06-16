package com.scd.gitlabtimeback.mapper;

import java.sql.Timestamp;
import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.scd.gitlabtimeback.dto.DateBoundaryDto;

@Component
public class DateBoundaryMapper {

    public Timestamp getFrom(DateBoundaryDto from) {

        if(from == null) return null;

        Calendar now = getCalendar(from);

        return new Timestamp(now.getTimeInMillis());
    }

    private Calendar getCalendar(DateBoundaryDto dateBoundary) {
        Calendar now = Calendar.getInstance();

        now.set(dateBoundary.getYear(), dateBoundary.getMonth(),dateBoundary.getDay());

        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        return now;
    }

    public Timestamp getTo(DateBoundaryDto to) {

        if(to == null) return null;

        Calendar now = getCalendar(to);

        now.add(Calendar.DAY_OF_YEAR, 1);

        return new Timestamp(now.getTimeInMillis());
    }
    
}
