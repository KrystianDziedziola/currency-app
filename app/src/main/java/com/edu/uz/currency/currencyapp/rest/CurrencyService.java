package com.edu.uz.currency.currencyapp.rest;

import com.edu.uz.currency.currencyapp.helper.RequestException;
import com.edu.uz.currency.currencyapp.helper.CurrencyMapper;
import com.edu.uz.currency.currencyapp.model.Currency;
import com.edu.uz.currency.currencyapp.rest.model.SingleCurrency;
import com.edu.uz.currency.currencyapp.rest.model.TableCurrency;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class CurrencyService {

    private static final String TABLE_A = "A";
    private final NbpClient nbpClient;

    public CurrencyService(final NbpClient nbpClient) {
        this.nbpClient = nbpClient;
    }

    public List<Currency> getAllCurrencies() throws IOException {
        final Response<List<TableCurrency>> response = nbpClient.getAllCurrencyFromTable(
                TABLE_A).execute();

        if (response.isSuccessful()) {
            final CurrencyMapper currencyMapper = new CurrencyMapper();
            return currencyMapper.mapAllCurrencies(response);
        } else {
            throw new RequestException();
        }
    }

    public Currency getSingleCurrency(final String code) throws IOException {
        final Response<SingleCurrency> response = nbpClient.getSingleCurrency(TABLE_A,
                code).execute();

        if (response.isSuccessful()) {
            final CurrencyMapper currencyMapper = new CurrencyMapper();
            return currencyMapper.mapSingleCurrency(response);
        } else {
            throw new RequestException();
        }
    }

    public List<Currency> getSingleCurrencyHistory(final String code,
                                                   final String startDate,
                                                   final String endDate) throws IOException {
        final Response<SingleCurrency> response = nbpClient.getSingleCurrencyHistory(TABLE_A, code,
                startDate, endDate).execute();

        if (response.isSuccessful()) {
            final CurrencyMapper currencyMapper = new CurrencyMapper();
            return currencyMapper.mapSingleCurrencyHistory(response);
        } else {
            throw new RequestException();
        }
    }
}