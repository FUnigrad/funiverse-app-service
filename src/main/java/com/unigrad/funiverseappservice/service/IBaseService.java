package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.specification.EntitySpecification;

import java.util.List;
import java.util.Optional;

public interface IBaseService<T, K> {

    List<T> getAll();

    List<T> getAllActive();

    Optional<T> get(K key);

    T save(T entity);

    void activate(K key);

    void inactivate(K key);

    boolean isExist(K key);

    List<T> search(EntitySpecification<T> specification);
}