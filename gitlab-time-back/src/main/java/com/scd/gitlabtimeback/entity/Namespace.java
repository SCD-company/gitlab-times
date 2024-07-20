package com.scd.gitlabtimeback.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;

import javax.persistence.Table;

@Getter
@Entity
@Table(name = "namespaces")
public class Namespace {

    @Id
    private Long id;

    private String path;
    
}
