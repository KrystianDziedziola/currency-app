package com.edu.uz.currency.currencyapp.service;

import com.edu.uz.currency.currencyapp.helper.RequestException;
import com.edu.uz.currency.currencyapp.helper.CurrencyMapper;
import com.edu.uz.currency.currencyapp.model.Currency;
import com.edu.uz.currency.currencyapp.rest.NbpClient;
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

    public List<Currency> getAllCurrencies() throws RequestException {
        try {
            final Response<List<TableCurrency>> response = nbpClient.getAllCurrencyFromTable(
                    TABLE_A).execute();

            final CurrencyMapper currencyMapper = new CurrencyMapper();
            return currencyMapper.mapAllCurrencies(response);
        } catch (final IOException e) {
            throw new RequestException();
        }
    }

    public Currency getSingleCurrency(final String code) throws RequestException {
        try {
            final Response<SingleCurrency> response = nbpClient.getSingleCurrency(
                    TABLE_A, code).execute();

            final CurrencyMapper currencyMapper = new CurrencyMapper();
            return currencyMapper.mapSingleCurrency(response);
        } catch (final IOException e) {
            throw new RequestException();
        }
    }

    /**
     *
     * @param code
     * @param startDate format "YYYY-MM-DD"
     * @param endDate format "YYYY-MM-DD"
     * @return
     * @throws RequestException
     */
    public List<Currency> getSingleCurrencyHistory(final String code,
                                                   final String startDate,
                                                   final String endDate) throws RequestException {
        try {
            final Response<SingleCurrency> response = nbpClient.getSingleCurrencyHistory(
                    TABLE_A, code, startDate, endDate).execute();

            final CurrencyMapper currencyMapper = new CurrencyMapper();
            return currencyMapper.mapSingleCurrencyHistory(response);
        } catch (final IOException e) {
            throw new RequestException();
        }
    }
}