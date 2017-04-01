package com.edu.uz.currency.currencyapp.service;

import com.edu.uz.currency.currencyapp.helper.RequestException;
import com.edu.uz.currency.currencyapp.model.Currency;
import com.edu.uz.currency.currencyapp.rest.NbpClient;
import com.edu.uz.currency.currencyapp.rest.model.SingleCurrency;
import com.edu.uz.currency.currencyapp.rest.model.SingleRate;
import com.edu.uz.currency.currencyapp.rest.model.TableCurrency;
import com.edu.uz.currency.currencyapp.rest.model.TableRate;
import com.edu.uz.currency.currencyapp.service.CurrencyService;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.mock.Calls;

import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class CurrencyServiceTest {

    private NbpClient nbpClient = mock(NbpClient.class);
    private CurrencyService currencyService = new CurrencyService(nbpClient);

    private Call tableCurrencyCall;
    private Call singleCurrencyCall;
    private Call singleCurrencyHistoryCall;

    @Before
    public void setUp() throws Exception {
        final TableRate firstTableRate = new TableRate("bat (Tajlandia)", "THB", 0.1144);
        final TableRate secondTableRate = new TableRate("dolar", "USD", 4.04);
        final List<TableRate> tableRates = Arrays.asList(firstTableRate, secondTableRate);
        final TableCurrency tableCurrency = new TableCurrency("A", "063/A/NBP/2017", "2017-03-30", tableRates);
        final List<TableCurrency> tableCurrencies = Collections.singletonList(tableCurrency);
        final Response<List<TableCurrency>> tableCurrencyResponse = Response.success(tableCurrencies);
        tableCurrencyCall = Calls.response(tableCurrencyResponse);

        final SingleRate singleRate = new SingleRate("069/A/NBP/2019", "2017-01-01", 9.63);
        final List<SingleRate> singleRates = Collections.singletonList(singleRate);
        final SingleCurrency singleCurrency = new SingleCurrency("A", "euro", "EUR", singleRates);
        final Response<SingleCurrency> singleCurrencyResponse = Response.success(singleCurrency);
        singleCurrencyCall = Calls.response(singleCurrencyResponse);

        final SingleRate secondSingleRate = new SingleRate("069/A/NBP/2019", "2017-01-02", 19.63);
        final List<SingleRate> singleRatesHistory = Arrays.asList(singleRate, secondSingleRate);
        final SingleCurrency singleCurrencyHistory = new SingleCurrency("A", "euro", "EUR", singleRatesHistory);
        final Response<SingleCurrency> singleCurrencyHistoryResponse = Response.success(singleCurrencyHistory);
        singleCurrencyHistoryCall = Calls.response(singleCurrencyHistoryResponse);
    }

    @Test
    public void Should_Get_All_Currencies_From_Table() throws Exception {
        //given
        given(nbpClient.getAllCurrencyFromTable("A")).willReturn(tableCurrencyCall);

        //when
        final List<Currency> currencies = currencyService.getAllCurrencies();

        //then
        assertThat(currencies).hasSize(2);
    }

    @Test
    public void Should_Get_Single_Currency_From_Table() throws Exception {
        //given
        final String code = "eur";
        final String expectedCurrency = "euro";

        given(nbpClient.getSingleCurrency("A", code)).willReturn(singleCurrencyCall);

        //when
        final Currency currency = currencyService.getSingleCurrency(code);

        //then
        assertThat(currency.getName()).isEqualTo(expectedCurrency);
    }

    @Test
    public void Should_Get_Single_Currency_History_From_Table() throws Exception {
        //given
        final String code = "eur";
        final String startDate = "2017-01-01";
        final String endDate = "2017-01-02";

        given(nbpClient.getSingleCurrencyHistory("A", code, startDate, endDate))
                .willReturn(singleCurrencyHistoryCall);

        //when
        final List<Currency> currencies = currencyService.getSingleCurrencyHistory(
                code, startDate, endDate);

        //then
        assertThat(currencies).hasSize(2);
        assertThat(currencies.get(0).getDate()).isEqualTo(LocalDate.parse("2017-01-01"));
        assertThat(currencies.get(1).getDate()).isEqualTo(LocalDate.parse("2017-01-02"));
    }

    @Test(expected = RequestException.class)
    public void Should_Throw_Exception_When_Request_Fails() throws RequestException {
        // given
        given(nbpClient.getAllCurrencyFromTable("A")).willThrow(IOException.class);

        // when
        currencyService.getAllCurrencies();

        // then
        // expect exception of class RequestException
    }
}