package com.scd.gitlabtimeback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DataFileDto {
    private String fileName;
    private byte[] content;
}
