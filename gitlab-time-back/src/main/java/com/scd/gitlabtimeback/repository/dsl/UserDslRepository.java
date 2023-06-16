package com.scd.gitlabtimeback.repository.dsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.scd.gitlabtimeback.entity.User;
import com.scd.gitlabtimeback.entity.QUser;

import org.springframework.stereotype.Repository;

import java.util.List;

import static com.scd.gitlabtimeback.entity.QUser.user;
import static com.scd.gitlabtimeback.entity.QTimeLog.timeLog;
import static com.scd.gitlabtimeback.entity.QProjectAuthorizations.projectAuthorizations;

@Repository
public class UserDslRepository extends DslRepository<User, QUser> {

    public UserDslRepository() {
        super(user);
    }

    public User findById(Long id) {
        return from(user)
                .select(user)
                .where(user.id.eq(id))
                .fetchOne();
    }

    public Boolean isUserExists(Long id) {
        return from(user)
                .select(user)
                .where(user.id.eq(id))
                .fetchOne() != null;

    }

    public List<User> findAllActiveByProjectId(Long currentUserId, Long projectId) {

        if (currentUserId == null) {
            return from(timeLog)
                    .where((projectId == null ? Expressions.TRUE.isTrue() : timeLog.project.id.eq(projectId))
                            .and(timeLog.user.state.eq("active")))
                    .select(timeLog.user)
                    .orderBy(timeLog.user.name.asc())
                    .distinct()
                    .fetch();
        } else {
            // we don't pay attention to the period. 
            // we will show everybody, having reports at least at one project where I have access
            return from(projectAuthorizations)
                    // where all the projects are within...
                    .where(new BooleanBuilder()
                            .and(projectAuthorizations.user.state.eq("active"))
                            .and(projectId == null ? Expressions.TRUE.isTrue()
                                    : projectAuthorizations.project.id.eq(projectId))
                            .and(

                                    new BooleanBuilder().or(projectAuthorizations.project.id.in(
                                            // ...those , current user has permissions to see
                                            JPAExpressions.select(projectAuthorizations.project.id)
                                                    .from(projectAuthorizations)
                                                    .where(projectAuthorizations.user.id.eq(currentUserId)
                                                            .and(projectAuthorizations.access_level.goe(Constants.MIN_USER_LEVEL_TO_SEE_ALL))))
                                             //...or current user owns this authorization
                                            .or(projectAuthorizations.user.id.eq(currentUserId))

                                    ))
                            // and projects are not archived
                            .and(projectAuthorizations.project.archived.isFalse())
                            // and each project has at least one report...
                            .and(JPAExpressions.selectOne().from(timeLog)
                                    .where(

                                            new BooleanBuilder()
                                                    .and(timeLog.project.id.eq(projectAuthorizations.project.id))
                                                    // ... from this (returning) user
                                                    .and(timeLog.user.id.eq(projectAuthorizations.user.id))

                                    ).exists()))
                    .select(projectAuthorizations.user)
                    .orderBy(projectAuthorizations.user.name.asc())
                    .distinct()
                    .fetch();
        }
    }

}
