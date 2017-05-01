package com.edu.uz.currency.currencyapp.model;

import com.edu.uz.currency.currencyapp.helper.ValidationHelper;

import java.util.Objects;

public class Money {

    private static final int MINIMUM_AMOUNT = 0;

    private final String currencyCode;
    private final double amount;

    public Money(final double amount, final String currencyCode) {
        ValidationHelper.validateMinimumValue("amount", amount, MINIMUM_AMOUNT);
        ValidationHelper.notNull("currencyCode", currencyCode);
        ValidationHelper.validateCurrencyCode(currencyCode);
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Double.compare(money.amount, amount) == 0 &&
                Objects.equals(currencyCode, money.currencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyCode, amount);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", amount, currencyCode);
    }
}
