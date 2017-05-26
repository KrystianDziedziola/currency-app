package com.edu.uz.currency.currencyapp.service;

import com.edu.uz.currency.currencyapp.model.Currency;
import com.edu.uz.currency.currencyapp.model.Money;

import java.util.List;

public class ExchangeService {

    private static final String POLISH_CURRENCY_CODE = "PLN";

    private final List<Currency> currencies;

    public ExchangeService(final List<Currency> currencies) {
        this.currencies = currencies;
    }

    public Money exchange(final Money moneyToExchange, final String resultCurrencyCode) {
        final Currency givenCurrency = getSingleCurrency(moneyToExchange.getCurrencyCode());
        final Currency resultCurrency = getSingleCurrency(resultCurrencyCode);

        final double givenCurrencyRate = givenCurrency.getRate();
        final double resultCurrencyRate = resultCurrency.getRate();
        final double amountToExchange = moneyToExchange.getAmount();
        final double resultAmount = amountToExchange * givenCurrencyRate / resultCurrencyRate;

        return new Money(resultAmount, resultCurrencyCode);
    }

    public Money exchangeToPolishMoney(final Money money) {
        final Currency currency = getSingleCurrency(money.getCurrencyCode());
        final double currencyRate = currency.getRate();
        final double amount = money.getAmount();
        final double result = currencyRate * amount;

        return new Money(result, POLISH_CURRENCY_CODE);
    }

    private Currency getSingleCurrency(final String currencyCode) {
        for (final Currency currency : currencies) {
            if (currency.getCode().equals(currencyCode)) {
                return currency;
            }
        }

        throw new IllegalArgumentException();
    }
}
