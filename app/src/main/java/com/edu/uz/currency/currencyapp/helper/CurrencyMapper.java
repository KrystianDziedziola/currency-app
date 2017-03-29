package com.edu.uz.currency.currencyapp.helper;

import com.edu.uz.currency.currencyapp.model.Currency;
import com.edu.uz.currency.currencyapp.rest.model.SingleCurrency;
import com.edu.uz.currency.currencyapp.rest.model.SingleRate;
import com.edu.uz.currency.currencyapp.rest.model.TableCurrency;
import com.edu.uz.currency.currencyapp.rest.model.TableRate;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class CurrencyMapper {

    public List<Currency> mapAllCurrencies(final Response<List<TableCurrency>> response) {
        final List<Currency> currencies = new ArrayList<>();

        final List<TableCurrency> tableCurrency = response.body();

        for (final TableCurrency currency : tableCurrency) {
            final List<TableRate> tableRates = currency.getRates();
            for (final TableRate tableRate : tableRates) {
                currencies.add(new Currency(tableRate.getCurrency(), tableRate.getCode(),
                        tableRate.getMid(), LocalDate.parse(currency.getEffectiveDate())));
            }
        }
        return currencies;
    }

    public Currency mapSingleCurrency(final Response<SingleCurrency> response) {
        final SingleCurrency singleCurrency = response.body();
        final SingleRate singleRate = singleCurrency.getRates().get(0);

        return new Currency(singleCurrency.getCurrency(), singleCurrency.getCode(),
                singleRate.getMid(), LocalDate.parse(singleRate.getEffectiveDate()));
    }

    public List<Currency> mapSingleCurrencyHistory(final Response<SingleCurrency> response) {
        final SingleCurrency singleCurrency = response.body();
        final List<SingleRate> singleRates = singleCurrency.getRates();
        final List<Currency> currencies = new ArrayList<>();

        for (final SingleRate singleRate : singleRates) {
            final Currency currency = new Currency(singleCurrency.getCurrency(),
                    singleCurrency.getCode(), singleRate.getMid(),
                    LocalDate.parse(singleRate.getEffectiveDate()));
            currencies.add(currency);
        }
        return currencies;
    }
}