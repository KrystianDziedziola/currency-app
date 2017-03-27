package com.edu.uz.currency.currencyapp.repository.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.edu.uz.currency.currencyapp.repository.CurrencyQuery;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Currency";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(CurrencyQuery.CREATE_CURRENCY_TABLE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

    }
}
