package com.scd.gitlabtimeback.entity;

import lombok.Getter;

import java.time.Instant;

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


    private long project_id;

    private Long iid;

    @Column(name="closed_at")
    private Instant closedAt;

}
