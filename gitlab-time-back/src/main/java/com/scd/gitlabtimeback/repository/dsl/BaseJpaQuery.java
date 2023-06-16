package com.scd.gitlabtimeback.repository.dsl;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import java.util.Objects;
import java.util.stream.Stream;

public class BaseJpaQuery<T> extends JPAQuery<T> {

    public BaseJpaQuery(final EntityManager em) {
        super(em);
    }

    @Override
    public JPAQuery<T> on(final Predicate condition) {
        return on(new Predicate[]{condition});
    }

    @Override
    public JPAQuery<T> on(final Predicate... conditions) {
        Predicate[] predicates = Stream.of(conditions)
                .filter(Objects::nonNull)
                .toArray(Predicate[]::new);

        return super.on(predicates);
    }

}
