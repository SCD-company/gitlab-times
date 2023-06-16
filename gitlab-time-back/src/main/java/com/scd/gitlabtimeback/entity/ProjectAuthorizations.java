package com.scd.gitlabtimeback.entity;

import lombok.Getter;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import javax.persistence.Id;

@Getter
@Entity
@Table(name = "project_authorizations")
public class ProjectAuthorizations implements Serializable{

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    Integer access_level;
}
