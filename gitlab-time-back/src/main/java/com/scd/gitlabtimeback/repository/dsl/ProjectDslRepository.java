package com.scd.gitlabtimeback.repository.dsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.scd.gitlabtimeback.entity.Project;
import com.scd.gitlabtimeback.entity.QProject;


import org.springframework.stereotype.Repository;

import java.util.List;

import static com.scd.gitlabtimeback.entity.QProject.project;
import static com.scd.gitlabtimeback.entity.QProjectAuthorizations.projectAuthorizations;
import static com.scd.gitlabtimeback.entity.QTimeLog.timeLog;

@Repository
public class ProjectDslRepository extends DslRepository<Project, QProject> {

    public ProjectDslRepository() {
        super(project);
    }

   
    public List<Project> findAllByUserId(Long currentUserId, Long userId) {
        if (currentUserId == null) {
            return from(timeLog)
                    .where(userId==null?Expressions.TRUE.isTrue():timeLog.user.id.eq(userId)) 
                    .where(timeLog.project.archived.isFalse())
                    .select(timeLog.project)
                    .orderBy(timeLog.project.name.asc())
                    .distinct()
                    .fetch();
        } else {
            return from(projectAuthorizations)
                    .where(new BooleanBuilder().and(projectAuthorizations.user.id.eq(currentUserId))
                            .and(projectAuthorizations.project.archived.isFalse())
                            .and(JPAExpressions.selectOne().from(timeLog)
                                    .where(
                                        new BooleanBuilder()
                                            .and(timeLog.project.id.eq(projectAuthorizations.project.id))
                                            .and(userId==null?Expressions.TRUE.isTrue():timeLog.user.id.eq(userId)) 
                                        ).exists()))
                    .select(projectAuthorizations.project)
                    .orderBy(projectAuthorizations.project.name.asc())
                    .distinct()
                    .fetch();
        }
    }

}
