package com.edu.uz.currency.currencyapp.retrofit;

import com.edu.uz.currency.currencyapp.retrofit.pojo.SingleCurrency;
import com.edu.uz.currency.currencyapp.retrofit.pojo.TableCurrency;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.*;

public class NbpClientTest {

    private NbpClient nbpClient;

    @Before
    public void setUp() throws Exception {
        nbpClient = NbpClient.FactoryNbpClient.getNbpClient();
    }

    @Test
    public void getAllCurrencyFromTable() throws Exception {
        //given
        final String table = "a";
        final String expectedTableName = "A";

        //when
        final List<TableCurrency> tableCurrencies = nbpClient.getAllCurrencyFromTable(table).execute().body();
        final String tableName = tableCurrencies.get(0).getTable();

        //then
        assertThat(tableName).isEqualTo(expectedTableName);
    }

    @Test
    public void getSingleCurrency() throws Exception {
        //given
        final String table = "a";
        final String currency = "eur";
        final String expectedCurrency = "euro";

        //when
        final SingleCurrency singleCurrency = nbpClient.getSingleCurrency(table, currency).execute().body();
        final String currencyResponse = singleCurrency.getCurrency();

        //then
        assertThat(currencyResponse).isEqualTo(expectedCurrency);
    }

    @Test
    public void getSingleCurrencyHistory() throws Exception {
        //given
        final String table = "a";
        final String currency = "eur";
        final String startDate = "2017-01-01";
        final String endDate = "2017-01-31";
        final int expectedRates = 21;

        //when
        final SingleCurrency singleCurrency = nbpClient.getSingleCurrencyHistory(table, currency, startDate, endDate).execute().body();
        final List rates = singleCurrency.getRates();

        //then
        assertThat(rates.size()).isEqualTo(expectedRates);
    }
}