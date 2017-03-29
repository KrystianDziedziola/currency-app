package com.edu.uz.currency.currencyapp.model;

import com.edu.uz.currency.currencyapp.helper.ValidationHelper;

import org.joda.time.LocalDate;

public class Currency {

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
}
