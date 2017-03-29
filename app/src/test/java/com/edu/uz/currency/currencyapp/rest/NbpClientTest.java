package com.edu.uz.currency.currencyapp.rest;

import com.edu.uz.currency.currencyapp.model.Currency;
import com.edu.uz.currency.currencyapp.rest.model.SingleCurrency;

import org.assertj.core.internal.cglib.core.Local;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static android.media.CamcorderProfile.get;
import static org.assertj.core.api.Java6Assertions.*;

public class NbpClientTest {

    private CurrencyService currencyService;

    @Before
    public void setUp() throws Exception {
        final NbpClient nbpClient = NbpClient.FactoryNbpClient.getNbpClient();
        currencyService = new CurrencyService(nbpClient);
    }

    @Test
    public void Should_Get_All_Currencies_From_Table() throws Exception {
        //given
        final String expectedCode = "THB";

        //when
        final List<Currency> currencies = currencyService.getAllCurrencies();
        final String code = currencies.get(0).getCode();

        //then
        assertThat(code).isEqualTo(expectedCode);
    }

    @Test
    public void Should_Get_Single_Currency_From_Table() throws Exception {
        //given
        final String code = "eur";
        final String expectedCurrency = "euro";

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
        final String endDate = "2017-01-31";
        final LocalDate expectedDate = LocalDate.parse("2017-01-04");

        //when
        final List<Currency> currencies = currencyService.getSingleCurrencyHistory(code, startDate,
                endDate);
        final LocalDate localDate = currencies.get(2).getDate();

        //then
        assertThat(localDate).isEqualTo(expectedDate);
    }
}