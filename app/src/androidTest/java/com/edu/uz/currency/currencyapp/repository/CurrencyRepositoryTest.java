package com.edu.uz.currency.currencyapp.repository;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.runner.AndroidJUnit4;

import com.edu.uz.currency.currencyapp.model.Currency;
import com.edu.uz.currency.currencyapp.repository.exception.CurrencyInsertException;
import com.edu.uz.currency.currencyapp.repository.helper.DatabaseHelper;
import com.edu.uz.currency.currencyapp.repository.specification.GetCurrencyByCodeSpecification;
import com.edu.uz.currency.currencyapp.repository.specification.Specification;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class CurrencyRepositoryTest {

    private final SQLiteOpenHelper sqLiteOpenHelper = new DatabaseHelper(getTargetContext());
    private Repository<Currency> repository = new CurrencyRepository(sqLiteOpenHelper);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void Set_Up() {
        repository.deleteAll();
    }

    @Test
    public void Should_Add_Currency() {
        // given
        final LocalDate date = LocalDate.now();
        final Currency currency = new Currency("Dolar", "USD", 4.15, date);

        // when
        repository.add(currency);
        final List<Currency> currencies = repository.getAll();

        // then
        assertEquals(1, currencies.size());
        assertEquals("Dolar", currencies.get(0).getName());
        assertEquals("USD", currencies.get(0).getCode());
        assertEquals(4.15, currencies.get(0).getRate());
        assertEquals(date, currencies.get(0).getDate());
    }

    @Test
    public void Should_Get_One_Currency_By_Code() {
        // given
        final LocalDate date = LocalDate.now();
        repository.add(new Currency("Dolar", "USD", 4.15, date));

        final Specification specification = new GetCurrencyByCodeSpecification();

        // when
        final Currency currency = repository.getOne(specification, "USD");

        // then
        assertNotNull(currency);
        assertEquals("Dolar", currency.getName());
        assertEquals("USD", currency.getCode());
        assertEquals(4.15, currency.getRate());
        assertEquals(date, currency.getDate());
    }

    @Test(expected = CurrencyInsertException.class)
    public void Should_Throw_Exception_When_Adding_Existing_Currency() throws CurrencyInsertException {
        // given
        final LocalDate date = LocalDate.now();
        final Currency currency = new Currency("Dolar", "USD", 4.15, date);
        repository.add(currency);

        // when
        repository.add(currency);

        // then
        // expected exception of class CurrencyInsertException
    }
}