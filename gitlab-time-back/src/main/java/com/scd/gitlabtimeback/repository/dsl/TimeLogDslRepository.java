package com.scd.gitlabtimeback.repository.dsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Coalesce;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.scd.gitlabtimeback.entity.TimeLog;
import com.scd.gitlabtimeback.enums.GroupingByField;
import com.scd.gitlabtimeback.entity.QTimeLog;

import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

import static com.scd.gitlabtimeback.entity.QTimeLog.timeLog;

import static com.scd.gitlabtimeback.entity.QProjectAuthorizations.projectAuthorizations;

@Repository
public class TimeLogDslRepository extends DslRepository<TimeLog, QTimeLog> {

  

    public TimeLogDslRepository() {
        super(timeLog);
    }

    

    public List<Tuple> findAllWithFiltersAndGrouping(Long currentUserId,
            Long userId,
            Long projectId,
            Long issueId,
            Timestamp since,
            Timestamp to,
            GroupingByField grouping,
            boolean useSpentTime) {

        var whereClause = createBooleanBuilder(currentUserId, userId, projectId, issueId, since, to, useSpentTime);

        var groupingId = grouping != GroupingByField.MONTH_SPENT ? grouping.getId()
                : new Coalesce<>(GroupingByField.MONTH_SPENT.getId(), GroupingByField.MONTH.getId());
        var groupingName = grouping != GroupingByField.MONTH_SPENT ? grouping.getName()
                : new Coalesce<>(GroupingByField.MONTH_SPENT.getName(), GroupingByField.MONTH.getName());

        var exp = fromNonDistinct(timeLog)
                .groupBy(groupingId,
                        groupingName)
                .where(whereClause)
                .select(groupingId,
                        groupingName,
                        timeLog.timeSpent.sum(),
                        getActual(grouping))
                .orderBy(new OrderSpecifier<>(Order.ASC, groupingName));
                

        if(grouping==GroupingByField.PROJECT) {
            exp = exp.groupBy(timeLog.project.archived);
        }
        if(grouping==GroupingByField.PERSON) {
            exp = exp.groupBy(timeLog.user.state);
        }

        return exp.fetch();
    }

    Expression<?> getActual(GroupingByField grouping) {

        switch(grouping) {
            case PROJECT:
              return timeLog.project.archived;
            case PERSON:
              return timeLog.user.state.ne("active");
            default:
               return Expressions.FALSE;
             
        }

       
    }

    private BooleanBuilder createBooleanBuilder(Long currentUserId,
            Long userId,
            Long projectId,
            Long issueId,
            Timestamp since,
            Timestamp to,
            boolean useSpentTime) {
        var whereClause = new BooleanBuilder();

    
        if (currentUserId != null) {
            // user is not admin
            // so we only allow HIS OWN reports
            // OR
            // reports for PROJECTS where he is the owner
            whereClause.and(new BooleanBuilder()
                    .or(timeLog.user.id.eq(currentUserId))
                    .or(timeLog.project.id
                            .in(JPAExpressions.select(projectAuthorizations.project.id).from(projectAuthorizations)
                                    .where(
                                            new BooleanBuilder()
                                                    
                                                    .and(projectAuthorizations.access_level.goe(Constants.MIN_USER_LEVEL_TO_SEE_ALL)
                                                    .and(projectAuthorizations.user.id.eq(currentUserId)))))));
        }

        if (userId != null) {
            whereClause.and(timeLog.user.id.eq(userId));
        }

        if (issueId != null) {
            whereClause.and(timeLog.issue.id.eq(issueId));
        } else {
            whereClause.and(timeLog.issue.id.isNotNull());
        }

        if (projectId != null) {
            whereClause.and(timeLog.project.id.eq(projectId));
        }

        if (since != null) {
            System.err.println("since: " + since);
            whereClause.and(useSpentTime ? new BooleanBuilder()
                    .or(new BooleanBuilder(timeLog.spentAt.isNotNull()).and(timeLog.spentAt.goe(since)))
                    .or(new BooleanBuilder(timeLog.spentAt.isNull()).and(timeLog.createdAt.goe(since)))
                    : timeLog.createdAt.goe(since));
        }

        if (to != null) {
            System.err.println("to: " + to);
            whereClause.and(useSpentTime ? new BooleanBuilder()
                    .or(new BooleanBuilder(timeLog.spentAt.isNotNull()).and(timeLog.spentAt.before(to)))
                    .or(new BooleanBuilder(timeLog.spentAt.isNull()).and(timeLog.createdAt.before(to)))
                    : timeLog.createdAt.before(to));
        }

        return whereClause;
    }

}
