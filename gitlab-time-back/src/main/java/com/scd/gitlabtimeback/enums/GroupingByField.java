package com.scd.gitlabtimeback.enums;

import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringExpression;

import static com.scd.gitlabtimeback.entity.QTimeLog.timeLog;


public enum GroupingByField {
    ISSUE(timeLog.issue.id, timeLog.issue.iid.stringValue().concat(timeLog.issue.title)),
    CLOSED(timeLog.issue.closedAt.yearMonth().castToNum(Long.class), timeLog.issue.closedAt.yearMonth().stringValue()),
    PROJECT(timeLog.project.id, timeLog.project.name),
    PERSON(timeLog.user.id, timeLog.user.name),
    MONTH(timeLog.createdAt.yearMonth().castToNum(Long.class), timeLog.createdAt.yearMonth().stringValue()),
    MONTH_SPENT(timeLog.spentAt.yearMonth().castToNum(Long.class), timeLog.spentAt.yearMonth().stringValue());

    private final NumberExpression<Long> id;
    private final StringExpression name;


    GroupingByField(NumberExpression<Long> id, StringExpression name) {
        this.id = id;
        this.name = name;
    }

    public NumberExpression<Long> getId() {
        return id;
    }

    public StringExpression getName() {
        return name;
    }
}
