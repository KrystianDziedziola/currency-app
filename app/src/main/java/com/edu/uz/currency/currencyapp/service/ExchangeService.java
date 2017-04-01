package com.edu.uz.currency.currencyapp.service;

import com.edu.uz.currency.currencyapp.helper.RequestException;
import com.edu.uz.currency.currencyapp.model.Currency;
import com.edu.uz.currency.currencyapp.model.Money;

public class ExchangeService {

    public static final String POLISH_CURRENCY_CODE = "PLN";
    private final CurrencyService currencyService;

    public ExchangeService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public Money exchange(final Money moneyToExchange, final String resultCurrencyCode) throws RequestException {
        final Currency givenCurrency = currencyService.getSingleCurrency(moneyToExchange.getCurrencyCode());
        final Currency resultCurrency = currencyService.getSingleCurrency(resultCurrencyCode);

        final double givenCurrencyRate = givenCurrency.getRate();
        final double resultCurrencyRate = resultCurrency.getRate();
        final double amountToExchange = moneyToExchange.getAmount();
        final double resultAmount = amountToExchange * givenCurrencyRate / resultCurrencyRate;

        return new Money(resultAmount, resultCurrencyCode);
    }

    public Money exchangeToPolishMoney(final Money money) throws RequestException {
        final Currency currency = currencyService.getSingleCurrency(money.getCurrencyCode());
        final double currencyRate = currency.getRate();
        final double amount = money.getAmount();
        final double result = currencyRate * amount;

        return new Money(result, POLISH_CURRENCY_CODE);
    }
}
