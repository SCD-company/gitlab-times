package com.scd.gitlabtimeback.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "issues")
public class Issue {
    @Id
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;
}
