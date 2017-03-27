package com.edu.uz.currency.currencyapp.repository;

import com.edu.uz.currency.currencyapp.model.Currency;

public class CurrencyQuery {

    public static String CREATE_CURRENCY_TABLE = String.format(
            "CREATE TABLE %s (%s TEXT, %s TEXT PRIMARY KEY, %s REAL, %s TEXT);",
            Currency.TABLE_NAME,
            Currency.NAME_KEY,
            Currency.CODE_KEY,
            Currency.RATE_KEY,
            Currency.DATE_KEY);

    static String GET_ALL_CURRENCIES = String.format("SELECT * FROM %s", Currency.TABLE_NAME);
    static String DELETE_ALL_CURRENCIES = String.format("DELETE FROM %s", Currency.TABLE_NAME);

}
