package com.scd.gitlabtimeback.enums;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Coalesce;

import static com.scd.gitlabtimeback.entity.QTimeLog.timeLog;

public enum GroupingByField {
    ISSUE(timeLog.issue.id, timeLog.issue.iid.stringValue().concat(timeLog.issue.title)),
    PROJECT(timeLog.project.id, timeLog.project.name),
    PERSON(timeLog.user.id, timeLog.user.name),
    MONTH(timeLog.createdAt.yearMonth().castToNum(Long.class), timeLog.createdAt.yearMonth().stringValue()),
    MONTH_SPENT(
            new Coalesce<>(timeLog.spentAt.yearMonth().castToNum(Long.class),
                    timeLog.createdAt.yearMonth().castToNum(Long.class)),
            new Coalesce<>(timeLog.spentAt.yearMonth().stringValue(), timeLog.createdAt.yearMonth().stringValue()));

    private final Expression<Long> id;
    private final Expression<String> name;

    GroupingByField(Expression<Long> id, Expression<String> name) {
        this.id = id;
        this.name = name;
    }

    public Expression<Long> getId() {
        return id;
    }

    public Expression<String> getName() {
        return name;
    }
}
