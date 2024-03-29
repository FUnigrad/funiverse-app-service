package com.unigrad.funiverseappservice.specification;

import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.Role;
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
                case "eq" -> {
                    if (criteria.getField().equalsIgnoreCase("type")) {
                        predicates.add(criteriaBuilder.equal(root.get(criteria.getField()), Group.Type.valueOf(criteria.getValue())));
                    } else if (criteria.getField().equalsIgnoreCase("role")) {
                        predicates.add(criteriaBuilder.equal(root.get(criteria.getField()), Role.valueOf(criteria.getValue())));
                    } else {
                        predicates.add(criteriaBuilder.equal(root.get(criteria.getField()), criteria.getValue()));
                    }
                }
                case "bool" -> {
                    if (criteria.getValue().equals("true")) {
                        predicates.add(criteriaBuilder.isTrue(root.get(criteria.getField())));
                    } else {
                        predicates.add(criteriaBuilder.isFalse(root.get(criteria.getField())));
                    }
                }
                case "nmt" -> {
                    predicates.add(criteriaBuilder.isNotEmpty(root.get(criteria.getField())));
                }
            }
        }

        if (searchCriteria.size() == 2
                && searchCriteria.stream().anyMatch(criteria -> criteria.getField().equalsIgnoreCase("name"))
                && searchCriteria.stream().anyMatch(criteria -> criteria.getField().equalsIgnoreCase("code"))) {
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        }

        predicates.add(criteriaBuilder.isTrue(root.get("isActive")));

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }


}