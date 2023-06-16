package com.scd.gitlabtimeback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

import com.scd.gitlabtimeback.enums.Period;

@Getter
@Setter
@AllArgsConstructor
public class PeriodDto {
    @Nullable
    private Period period;
    private String value;
}
