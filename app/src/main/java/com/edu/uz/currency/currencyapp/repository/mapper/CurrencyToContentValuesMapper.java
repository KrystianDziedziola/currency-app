package com.edu.uz.currency.currencyapp.repository.mapper;

import android.content.ContentValues;

import com.edu.uz.currency.currencyapp.model.Currency;

public class CurrencyToContentValuesMapper implements Mapper<Currency, ContentValues> {

    @Override
    public ContentValues map(final Currency currency) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Currency.NAME_KEY, currency.getName());
        contentValues.put(Currency.CODE_KEY, currency.getCode());
        contentValues.put(Currency.RATE_KEY, currency.getRate());
        contentValues.put(Currency.DATE_KEY, currency.getDate().toString());

        return contentValues;
    }
}
