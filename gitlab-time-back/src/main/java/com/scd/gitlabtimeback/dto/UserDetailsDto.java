package com.scd.gitlabtimeback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto {
    Boolean authenticated = false;
    Long id;
    String name;
    Boolean admin;
}
