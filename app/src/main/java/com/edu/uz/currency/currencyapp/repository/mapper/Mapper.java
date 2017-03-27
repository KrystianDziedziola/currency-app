package com.edu.uz.currency.currencyapp.repository.mapper;

public interface Mapper<From, To> {
    To map(From from);
}
