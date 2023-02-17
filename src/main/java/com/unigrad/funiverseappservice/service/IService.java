package com.unigrad.funiverseappservice.service;

import java.util.List;
import java.util.Optional;

public interface IService<T, K> {

    List<T> getAll();

    Optional<T> get(K key);

    T save(T entity);

    void activate(K key);

    void deactivate(K key);
}