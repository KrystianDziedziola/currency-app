package com.edu.uz.currency.currencyapp.helper;

import com.edu.uz.currency.currencyapp.model.Currency;
import com.edu.uz.currency.currencyapp.rest.model.SingleCurrency;
import com.edu.uz.currency.currencyapp.rest.model.SingleRate;
import com.edu.uz.currency.currencyapp.rest.model.TableCurrency;
import com.edu.uz.currency.currencyapp.rest.model.TableRate;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;

import static org.assertj.core.api.Java6Assertions.*;


public class CurrencyMapperTest {

    private Response<List<TableCurrency>> tableCurrencyResponse;
    private Response<SingleCurrency> singleCurrencyResponse;


    @Before
    public void setUp() throws Exception {
        final TableRate tableRate = new TableRate("bat (Tajlandia)", "THB", 0.1144);
        final List<TableRate> tableRates = Collections.singletonList(tableRate);
        final TableCurrency tableCurrency = new TableCurrency("A", "063/A/NBP/2017", "2017-03-30", tableRates);
        final List<TableCurrency> tableCurrencies = Collections.singletonList(tableCurrency);

        final SingleRate singleRate = new SingleRate("069/A/NBP/2019", "2016-02-20", 9.63);
        final SingleRate singleRate1 = new SingleRate("069/A/NBP/2019", "2016-02-18", 19.63);

        final List<SingleRate> singleRates = new ArrayList<>(Arrays.asList(singleRate, singleRate1));
        final SingleCurrency singleCurrency = new SingleCurrency("A", "euro", "EUR", singleRates);

        tableCurrencyResponse = Response.success(tableCurrencies);
        singleCurrencyResponse = Response.success(singleCurrency);
    }

    @Test
    public void Should_Map_All_Currencies_To_Currency_Type() throws Exception {
        //given
        final String name = "bat (Tajlandia)";
        final LocalDate date = LocalDate.parse("2017-03-30");
        final String code = "THB";
        final double rate = 0.1144;
        final Currency currency = new Currency(name, code, rate, date);

        //when
        final CurrencyMapper currencyMapper = new CurrencyMapper();
        final List<Currency> currencies = currencyMapper.mapAllCurrencies(tableCurrencyResponse);

        //then
        assertThat(currencies.get(0)).isEqualToComparingFieldByField(currency);
    }

    @Test
    public void Should_Map_Currency_To_Currency_Type() throws Exception {
        //given
        final String name = "euro";
        final LocalDate date = LocalDate.parse("2016-02-20");
        final String code = "EUR";
        final double rate = 9.63;
        final Currency expectedCurrency = new Currency(name, code, rate, date);

        //when
        final CurrencyMapper currencyMapper = new CurrencyMapper();
        final Currency currency = currencyMapper.mapSingleCurrency(singleCurrencyResponse);

        //then
        assertThat(currency).isEqualToComparingFieldByField(expectedCurrency);
    }

    @Test
    public void Should_Map_Currency_History_To_Currency_Type() throws Exception {
        //given
        final String name = "euro";
        final String code = "EUR";
        final LocalDate date = LocalDate.parse("2016-02-20"), date1 = LocalDate.parse("2016-02-18");
        final double rate = 9.63, rate1 = 19.63;
        final Currency expectedCurrency = new Currency(name, code, rate, date);
        final Currency expectedCurrency1 = new Currency(name, code, rate1, date1);

        //when
        final CurrencyMapper currencyMapper = new CurrencyMapper();
        final List<Currency> currencies = currencyMapper.mapSingleCurrencyHistory(singleCurrencyResponse);

        //then
        assertThat(currencies.get(0)).isEqualToComparingFieldByField(expectedCurrency);
        assertThat(currencies.get(1)).isEqualToComparingFieldByField(expectedCurrency1);
    }
}