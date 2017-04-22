package com.edu.uz.currency.currencyapp.model;

import org.joda.time.LocalDate;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.*;

public class CurrencyTest {

    @Test
    public void Should_Create_Object() {
        // given
        final String name = "Dolar";
        final String code = "USD";
        final double rate = 4.05;
        final LocalDate date = LocalDate.now();

        // when
        final Currency currency = new Currency(name, code, rate, date);

        // then
        assertThat(currency).isNotNull();
        assertThat(currency.getName()).isEqualTo(name);
        assertThat(currency.getCode()).isEqualTo(code);
        assertThat(currency.getRate()).isEqualTo(rate);
        assertThat(currency.getDate()).isEqualTo(date);
    }

    @Test
    public void Should_Throw_Exception_When_Creating_Object_With_Wrong_Code() {
        // given
        final String wrongCode = "USDEE";

        // when
        try {
            new Currency("Dolar", wrongCode, 3.51, LocalDate.now());
        } catch (IllegalArgumentException e) {

        // then
            assertThat(e).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Wrong code 'USDEE'. It should contain 3 letters");
        }
    }

    @Test
    public void Should_Throw_Exception_When_Creating_Object_With_Wrong_Rate() {
        // given
        final double wrongRate = -0.5;

        // when
        try {
            new Currency("Dolar", "USD", wrongRate, LocalDate.now());
        } catch (IllegalArgumentException e) {

        // then
            assertThat(e).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Wrong value '-0.5' of field 'rate'. It should be bigger than '0'");
        }
    }

    @Test
    public void Should_Throw_Exception_When_Creating_Object_With_Null_Parameter() {
        // given
        final LocalDate nullDate = null;

        // when
        try {
            new Currency("Dolar", "USD", 4.04, nullDate);
        } catch (IllegalArgumentException e) {

        // then
            assertThat(e).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Parameter 'date' shouldn't be null");
        }
    }
}