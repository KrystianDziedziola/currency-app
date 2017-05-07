package com.edu.uz.currency.currencyapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.edu.uz.currency.currencyapp.R;
import com.edu.uz.currency.currencyapp.model.Currency;
import com.edu.uz.currency.currencyapp.rest.NbpClient;
import com.edu.uz.currency.currencyapp.service.CurrencyService;
import com.edu.uz.currency.currencyapp.ui.adapter.CurrenciesAdapter;

import java.io.IOException;
import java.util.List;

public class CurrencyAboutActivity extends AppCompatActivity {

    private final static String EXTRA_CURRENCY_CODE = "EXTRA_CURRENCY_CODE";
    private String currencyCode;
    private List<Currency> currencyHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_about);
        currencyCode = getIntent().getStringExtra(EXTRA_CURRENCY_CODE);

        new CurrencyAboutActivity.GetCurrencyTask().execute();
        setTitle(currencyCode);
    }

    public static void start(Activity activity, String currencyCode) {
        Intent intent = new Intent(activity, CurrencyAboutActivity.class);
        intent.putExtra(EXTRA_CURRENCY_CODE, currencyCode);
        activity.startActivity(intent);
    }

    class GetCurrencyTask extends AsyncTask<Void, Void, List<Currency>> {
        @Override
        protected List<Currency> doInBackground(Void... params) {
            List<Currency> currencies = null;
            try {
                String startDate = "2016-01-05";
                String endDate = "2017-01-05";
                currencies = new CurrencyService(NbpClient.FactoryNbpClient.getNbpClient()).getSingleCurrencyHistory(currencyCode, startDate, endDate);
            } catch (IOException e) {
                Log.e("CurrencyAboutActivity", "onClick: ", e);
            }
            return currencies;
        }

        @Override
        protected void onPostExecute(List<Currency> currencies) {
            super.onPostExecute(currencies);
            currencyHistory = currencies;
        }
    }
}
