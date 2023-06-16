package com.scd.gitlabtimeback.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "projects")
public class Project {
    @Id
    private Long id;

    private String name;

    private String path;

    private String description;

    private boolean archived;
}
