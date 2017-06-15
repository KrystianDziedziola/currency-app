package com.edu.uz.currency.currencyapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.edu.uz.currency.currencyapp.R;
import com.edu.uz.currency.currencyapp.databinding.ActivityCurrencyCalculatorBinding;
import com.edu.uz.currency.currencyapp.helper.RequestException;
import com.edu.uz.currency.currencyapp.model.Currency;
import com.edu.uz.currency.currencyapp.model.Money;
import com.edu.uz.currency.currencyapp.repository.CurrencyRepository;
import com.edu.uz.currency.currencyapp.repository.helper.DatabaseHelper;
import com.edu.uz.currency.currencyapp.rest.NbpClient;
import com.edu.uz.currency.currencyapp.service.CurrencyService;
import com.edu.uz.currency.currencyapp.service.ExchangeService;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class CurrencyCalculatorActivity extends AppCompatActivity {

    private ActivityCurrencyCalculatorBinding binding;
    private final Context context = this;
    private final CurrencyService currencyService = new CurrencyService(NbpClient.FactoryNbpClient.getNbpClient());
    private final CurrencyRepository currencyRepository = new CurrencyRepository(new DatabaseHelper(context));

    private List<Currency> currencies;
    private ExchangeService exchangeService;

    private Spinner fromCurrencySpinner;
    private Spinner toCurrencySpinner;
    private EditText amountEditText;
    private EditText resultTextView;

    private static final int CURRENCY_CODE_BEGIN_INDEX = 0;
    private static final int CURRENCY_CODE_END_INDEX = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_currency_calculator);
        setPolicy();
        initializeComponents();
        loadCurrencies();
        setDefaultValues();

        setTitle(getString(R.string.currencies_calculator));
    }

    private void loadCurrencies() {
        try {
            currencies = getCurrencies();
            exchangeService = new ExchangeService(currencies);
        } catch (final RequestException e) {
            showInternetConnectionError();
            return;
        }

        final List<String> currencyCodes = getCurrencyCodes(currencies);
        final ArrayAdapter<String> currenciesAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, currencyCodes);
        fromCurrencySpinner.setAdapter(currenciesAdapter);
        toCurrencySpinner.setAdapter(currenciesAdapter);
    }

    public static void start(Activity activity) {
        final Intent intent = new Intent(activity, CurrencyCalculatorActivity.class);
        activity.startActivity(intent);
    }

    private void setPolicy() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void initializeComponents() {
        fromCurrencySpinner = binding.fromCurrencySpinner;
        toCurrencySpinner = binding.toCurrencySpinner;
        amountEditText = binding.exchangeAmountEditText;
        resultTextView = binding.exchangeToEdittext;

        binding.swapCurrenciesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) fromCurrencySpinner.getSelectedItemId();
                fromCurrencySpinner.setSelection((int) toCurrencySpinner.getSelectedItemId());
                toCurrencySpinner.setSelection(temp);
                if (!binding.exchangeAmountEditText.getText().toString().trim().equals("")) {
                    final Money fromMoney = createFromMoney();
                    exchange(fromMoney);
                }
            }
        });


        binding.exchangeAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final Money fromMoney = createFromMoney();
                exchange(fromMoney);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setDefaultValues() {
        int posDefaultFrom = 0;
        int posDefaultTo = 0;
        for (int i = 0; i < currencies.size(); i++) {
            Currency c = currencies.get(i);
            if (c.getCode().equals("PLN")) {
                posDefaultTo = i;
            }
            if (c.getCode().equals("USD")) {
                posDefaultFrom = i;
            }
        }
        fromCurrencySpinner.setSelection(posDefaultFrom);
        toCurrencySpinner.setSelection(posDefaultTo);
    }

    private List<Currency> getCurrencies() throws RequestException {
        try {
            final List<Currency> currencies = currencyService.getAllCurrencies();
            currencyRepository.deleteAll();
            currencyRepository.add(currencies);
            currencies.add(new Currency("Polski złoty", "PLN", 1, new LocalDate()));
            return currencies;
        } catch (final RequestException e) {
            final List<Currency> currencies = currencyRepository.getAll();
            if (currencies.isEmpty()) {
                throw e;
            }
            showOldDataAlertDialog(getUpdateDate(currencies));
            return currencies;
        }
    }

    private String getUpdateDate(final List<Currency> currencies) {
        return currencies.get(0).getDate().toString();
    }

    private List<String> getCurrencyCodes(final List<Currency> currencies) {
        final List<String> codes = new ArrayList<>();
        for (final Currency currency : currencies) {
            final String currencyText = String.format("%s - %s", currency.getCode(), currency.getName());
            codes.add(currencyText);
        }

        return codes;
    }

    private void showInternetConnectionError() {
        new AlertDialog.Builder(context)
                .setTitle(R.string.no_internet)
                .setMessage(R.string.calculator_no_internet_message)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        CurrencyCalculatorActivity.this.finish();
                    }
                }).show();
    }

    private void showOldDataAlertDialog(final String updateDate) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.no_internet)
                .setMessage(getString(R.string.old_data_message, updateDate))
                .show();
    }

    private void exchange(final Money fromMoney) {
        final String toCurrency = parseCurrencyCode((String) toCurrencySpinner.getSelectedItem());
        final Money result = exchangeService.exchange(fromMoney, toCurrency);
        resultTextView.setText(result.toString());
    }

    private Money createFromMoney() {
        final double amount = Double.parseDouble(amountEditText.getText().toString());
        final String fromCurrency = parseCurrencyCode((String) fromCurrencySpinner.getSelectedItem());
        return new Money(amount, fromCurrency);
    }

    private String parseCurrencyCode(final String text) {
        return text.substring(CURRENCY_CODE_BEGIN_INDEX, CURRENCY_CODE_END_INDEX);
    }
}
