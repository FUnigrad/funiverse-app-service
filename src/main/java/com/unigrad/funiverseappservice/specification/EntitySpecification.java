package com.unigrad.funiverseappservice.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EntitySpecification<T> implements Specification<T> {

    private final List<SearchCriteria> searchCriteria;

    public EntitySpecification(List<SearchCriteria> searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : searchCriteria) {
            switch (criteria.getOperator()) {
                case "like" ->
                        predicates.add(criteriaBuilder.like(root.get(criteria.getField()), "%" + criteria.getValue() + "%"));
                case "eq" -> predicates.add(criteriaBuilder.equal(root.get(criteria.getField()), criteria.getValue()));
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }


}