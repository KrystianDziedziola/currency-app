package com.edu.uz.currency.currencyapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.uz.currency.currencyapp.R;
import com.edu.uz.currency.currencyapp.helper.RequestException;
import com.edu.uz.currency.currencyapp.model.Currency;
import com.edu.uz.currency.currencyapp.model.Money;
import com.edu.uz.currency.currencyapp.repository.CurrencyRepository;
import com.edu.uz.currency.currencyapp.repository.helper.DatabaseHelper;
import com.edu.uz.currency.currencyapp.rest.NbpClient;
import com.edu.uz.currency.currencyapp.service.CurrencyService;
import com.edu.uz.currency.currencyapp.service.ExchangeService;

import java.util.ArrayList;
import java.util.List;

public class CurrencyCalculatorActivity extends AppCompatActivity {

    private final Context context = this;
    private final CurrencyService currencyService = new CurrencyService(NbpClient.FactoryNbpClient.getNbpClient());
    private final CurrencyRepository currencyRepository = new CurrencyRepository(new DatabaseHelper(context));

    private List<Currency> currencies;
    private ExchangeService exchangeService;

    private Spinner fromCurrencySpinner;
    private Spinner toCurrencySpinner;
    private CheckBox toPlnCheckBox;
    private EditText amountEditText;
    private Button calculateButton;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_calculator);
        setPolicy();
        initializeComponents();
        loadCurrencies();
        addToPlnCheckBoxBehaviour();
        addCalculateButtonBehaviour();
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
        fromCurrencySpinner = (Spinner) findViewById(R.id.fromCurrencySpinner);
        toCurrencySpinner = (Spinner) findViewById(R.id.toCurrencySpinner);
        toPlnCheckBox = (CheckBox) findViewById(R.id.toPlnCheckBox);
        amountEditText = (EditText) findViewById(R.id.exchangeAmountEditText);
        calculateButton = (Button) findViewById(R.id.calculateButton);
        resultTextView = (TextView) findViewById(R.id.resultText);
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

    private List<Currency> getCurrencies() throws RequestException {
        try {
            final List<Currency> currencies = currencyService.getAllCurrencies();
            currencyRepository.deleteAll();
            currencyRepository.add(currencies);
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

    private void addToPlnCheckBoxBehaviour() {
        toPlnCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton view, final boolean isChecked) {
                toCurrencySpinner.setEnabled(!isChecked);
            }
        });
    }

    private void showToast(final int stringId) {
        Toast.makeText(getApplicationContext(), getString(stringId), Toast.LENGTH_SHORT).show();
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

    private void addCalculateButtonBehaviour() {
        calculateButton.setOnClickListener(new View.OnClickListener() {
            private static final int CURRENCY_CODE_BEGIN_INDEX = 0;
            private static final int CURRENCY_CODE_END_INDEX = 3;

            @Override
            public void onClick(final View view) {
                if (amountEditText.getText().toString().isEmpty()) {
                    showToast(R.string.calculator_empty_value_message);
                    return;
                }

                final Money fromMoney = createFromMoney();
                if (toPlnCheckBox.isChecked()) {
                    exchangeToPln(fromMoney);
                } else {
                    exchange(fromMoney);
                }
            }

            private void exchangeToPln(final Money fromMoney) {
                final Money result = exchangeService.exchangeToPolishMoney(fromMoney);
                resultTextView.setText(result.toString());
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
        });
    }
}
