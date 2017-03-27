package com.edu.uz.currency.currencyapp.repository.specification;

import com.edu.uz.currency.currencyapp.model.Currency;

public class GetCurrencyByCodeSpecification implements Specification {

    @Override
    public String toSqlQuery() {
        return String.format("SELECT * FROM %s WHERE code = ?", Currency.TABLE_NAME);
    }
}
