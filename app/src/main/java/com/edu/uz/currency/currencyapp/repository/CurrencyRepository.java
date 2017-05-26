package com.edu.uz.currency.currencyapp.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.edu.uz.currency.currencyapp.model.Currency;
import com.edu.uz.currency.currencyapp.repository.exception.CurrencyInsertException;
import com.edu.uz.currency.currencyapp.repository.mapper.CurrencyToContentValuesMapper;
import com.edu.uz.currency.currencyapp.repository.mapper.CursorToCurrencyMapper;
import com.edu.uz.currency.currencyapp.repository.mapper.Mapper;
import com.edu.uz.currency.currencyapp.repository.specification.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CurrencyRepository implements Repository<Currency> {

    public static final int INSERT_ERROR_RESPONSE_CODE = -1;
    private final SQLiteOpenHelper sqLiteOpenHelper;

    private final Mapper<Currency, ContentValues> currencyToContentValuesMapper;
    private final Mapper<Cursor, Currency> cursorToCurrencyMapper;

    public CurrencyRepository(final SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;

        this.currencyToContentValuesMapper = new CurrencyToContentValuesMapper();
        this.cursorToCurrencyMapper = new CursorToCurrencyMapper();
    }

    @Override
    public void add(final Currency currency) {
        add(Collections.singletonList(currency));
    }

    @Override
    public void add(final List<Currency> currencies) {
        final SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();

        try {
            database.beginTransaction();
            for (final Currency currency : currencies) {
                final ContentValues contentValues = currencyToContentValuesMapper.map(currency);
                long responseCode = database.insert(Currency.TABLE_NAME, null, contentValues);

                if (responseCode == INSERT_ERROR_RESPONSE_CODE) {
                    throw new CurrencyInsertException(currency.getCode());
                }
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @Override
    public Currency getOne(final Specification specification, final String... parameters) {
        try (final SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase()) {
            final Cursor cursor = database.rawQuery(specification.toSqlQuery(), parameters);
            cursor.moveToFirst();
            return cursorToCurrencyMapper.map(cursor);
        }
    }

    @Override
    public List<Currency> getAll() {
        final List<Currency> currencies = new ArrayList<>();

        try (final SQLiteDatabase database = sqLiteOpenHelper.getReadableDatabase()) {

            try (final Cursor cursor = database.rawQuery(CurrencyQuery.GET_ALL_CURRENCIES, null)) {
                if (cursor.moveToFirst()) {
                    do {
                        final Currency currency = cursorToCurrencyMapper.map(cursor);
                        currencies.add(currency);
                    } while (cursor.moveToNext());
                }
            }
            return currencies;
        }
    }

    @Override
    public void deleteAll() {
        try (final SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase()) {
            database.execSQL(CurrencyQuery.DELETE_ALL_CURRENCIES);
        }
    }
}
