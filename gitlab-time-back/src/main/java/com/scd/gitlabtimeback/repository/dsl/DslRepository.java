package com.scd.gitlabtimeback.repository.dsl;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public class DslRepository<T, Q extends EntityPathBase<T>> {

    @PersistenceContext()
    private EntityManager em;

    protected Q qClass;

    public DslRepository(Q qClass) {
        this.qClass = qClass;
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    protected <P> JPAQuery<P> from(final EntityPath<?>... entityPaths) {
        return new BaseJpaQuery<P>(em).from(entityPaths).distinct();
    }

    protected <P> JPAQuery<P> fromNonDistinct(final EntityPath<?>... entityPaths) {
        return new BaseJpaQuery<P>(em).from(entityPaths);
    }

    protected Optional<T> find(final JPAQuery<?> query) {
        T result = query.select(qClass).fetchOne();
        return Optional.ofNullable(result);
    }

    protected Optional<T> findAny(final JPAQuery<?> query) {
        T result = query.select(qClass).fetchFirst();
        return Optional.ofNullable(result);
    }

    public List<T> getAll() {
        return from(qClass).select(qClass).fetch();
    }


    public void resetCache() {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
    }
}
