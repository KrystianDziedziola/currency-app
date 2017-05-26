package com.edu.uz.currency.currencyapp.repository.mapper;

import android.database.Cursor;

import com.edu.uz.currency.currencyapp.model.Currency;

import org.joda.time.LocalDate;

public class CursorToCurrencyMapper implements Mapper<Cursor, Currency> {

    @Override
    public Currency map(final Cursor cursor) {
        final String name = cursor.getString(cursor.getColumnIndex(Currency.NAME_KEY));
        final String code = cursor.getString(cursor.getColumnIndex(Currency.CODE_KEY));
        final double rate = cursor.getDouble(cursor.getColumnIndex(Currency.RATE_KEY));
        final String dateString = cursor.getString(cursor.getColumnIndex(Currency.DATE_KEY));
        final LocalDate date = LocalDate.parse(dateString);

        return new Currency(name, code, rate, date);
    }
}
