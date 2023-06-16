package com.scd.gitlabtimeback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class DateBoundaryDto {
    private int year;
    private int month;
    private int day;
}
