package com.cg.service;

import java.util.List;
import java.util.Optional;

public interface IGeneralService<T, K> {

    List<T> findAll();

    Optional<T> findById(K id);

    T save(T t);

    void delete(T t);

    void deleteById(K id);
}
