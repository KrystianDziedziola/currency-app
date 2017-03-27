package com.edu.uz.currency.currencyapp.model;

import com.edu.uz.currency.currencyapp.helper.ValidationHelper;

import org.joda.time.LocalDate;

import java.util.Objects;

public class Currency {

    public static final String TABLE_NAME = "Currency";
    public static final String NAME_KEY = "name";
    public static final String CODE_KEY = "code";
    public static final String RATE_KEY = "rate";
    public static final String DATE_KEY = "date";

    private static final int CODE_LENGTH = 3;
    private static final int MINIMUM_RATE = 0;

    private String name;
    private String code;
    private double rate;
    private LocalDate date;

    public Currency(final String name, final String code, final double rate, final LocalDate date) {
        ValidationHelper.notNull("name", name);
        ValidationHelper.notNull("code", code);
        ValidationHelper.notNull("rate", rate);
        ValidationHelper.notNull("date", date);
        ValidationHelper.validateCurrencyCode(code, CODE_LENGTH);
        ValidationHelper.validateCurrencyRate(rate, MINIMUM_RATE);

        this.name = name;
        this.code = code;
        this.rate = rate;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public double getRate() {
        return rate;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Double.compare(currency.rate, rate) == 0 &&
                Objects.equals(name, currency.name) &&
                Objects.equals(code, currency.code) &&
                Objects.equals(date, currency.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code, rate, date);
    }
}
