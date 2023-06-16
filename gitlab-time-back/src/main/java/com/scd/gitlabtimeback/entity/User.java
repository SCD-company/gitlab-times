package com.scd.gitlabtimeback.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    private Long id;

    private String email;

    private String name;

    private Boolean admin;

    private String username;

    private String state;

    private int user_type;
}
