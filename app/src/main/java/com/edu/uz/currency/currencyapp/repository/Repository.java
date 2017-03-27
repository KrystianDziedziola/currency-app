package com.edu.uz.currency.currencyapp.repository;

import com.edu.uz.currency.currencyapp.repository.specification.Specification;

import java.util.List;

interface Repository<T> {

    void add(T item);
    void add(List<T> items);
    T getOne(final Specification specification, final String ... parameters);
    List<T> getAll();
    void deleteAll();
}
