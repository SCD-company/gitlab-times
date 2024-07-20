package com.scd.gitlabtimeback.enums;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Coalesce;
import com.querydsl.core.types.dsl.Expressions;

import static com.scd.gitlabtimeback.entity.QTimeLog.timeLog;

import java.util.List;

public enum GroupingByField {
    ISSUE(timeLog.issue.id,
            List.of(timeLog.issue.iid.stringValue(), timeLog.issue.title,
                    timeLog.issue.closedAt.yearMonth().stringValue()),
            timeLog.issue.iid.stringValue().concat(" ")
                    .concat(new Coalesce<>(timeLog.issue.closedAt.yearMonth().stringValue(),
                            Expressions.FALSE.stringValue()))
                    .concat(" ").concat(timeLog.issue.project_id.max().stringValue()).concat(" ")
                    .concat(timeLog.issue.title),
            List.of("ISSUE ID", "CLOSED AT", "ISSUE")),
    PROJECT(timeLog.project.id, timeLog.project.name),
    PERSON(timeLog.user.id, timeLog.user.name),
    MONTH(timeLog.createdAt.yearMonth().castToNum(Long.class), timeLog.createdAt.yearMonth().stringValue()),
    MONTH_SPENT(
            new Coalesce<>(timeLog.spentAt.yearMonth().castToNum(Long.class),
                    timeLog.createdAt.yearMonth().castToNum(Long.class)),
            new Coalesce<>(timeLog.spentAt.yearMonth().stringValue(), timeLog.createdAt.yearMonth().stringValue()));

    private final Expression<Long> id;
    private final List<Expression<String>> grouping;
    private final Expression<String> select;
    private final List<String> headers;

    GroupingByField(Expression<Long> id, Expression<String> name) {
        this.id = id;
        this.grouping = List.of(name);
        this.select = name;
        this.headers = List.of(name());
    }

    GroupingByField(Expression<Long> id, List<Expression<String>> grouping, Expression<String> select,
            List<String> headers) {
        this.id = id;
        this.grouping = grouping;
        this.select = select;
        this.headers = headers;
    }

    public Expression<Long> getId() {
        return id;
    }

    @SuppressWarnings("unchecked")
    public Expression<String>[] getGrouping() {
        return grouping.toArray(new Expression[0]);
    }

    public Expression<String> getSelect() {
        return select;
    }

    public List<String> getHeaders() {
        return headers;
    }
}
