package com.scd.gitlabtimeback.entity;

import lombok.Getter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Entity
@Table(name = "timelogs")
public class TimeLog {
    @Id
    private Long id;

    @Column(name = "time_spent")
    private Double timeSpent;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Issue issue;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @Column(name = "created_at")
    Timestamp createdAt;

    @Column(name = "spent_at")
    Timestamp spentAt;
}
