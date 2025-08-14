package com.example.vehicleinspection.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {
    T save(T entity);
    List<T> saveAll(List<T> entities);
    Optional<T> findById(ID id);
    boolean existsById(ID id);
    List<T> findAll();
    Page<T> findAll(Pageable pageable);
    long count();
    void deleteById(ID id);
    void delete(T entity);
    void deleteAll(List<T> entities);
}