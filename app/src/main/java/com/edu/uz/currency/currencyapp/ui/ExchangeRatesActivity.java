package com.edu.uz.currency.currencyapp.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.edu.uz.currency.currencyapp.R;
import com.edu.uz.currency.currencyapp.databinding.ActivityExchangeRatesBinding;
import com.edu.uz.currency.currencyapp.model.Currency;
import com.edu.uz.currency.currencyapp.repository.CurrencyRepository;
import com.edu.uz.currency.currencyapp.repository.helper.DatabaseHelper;
import com.edu.uz.currency.currencyapp.rest.NbpClient;
import com.edu.uz.currency.currencyapp.service.CurrencyService;
import com.edu.uz.currency.currencyapp.ui.adapter.CurrenciesAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesActivity extends AppCompatActivity {

    private final CurrencyRepository repository = new CurrencyRepository(new DatabaseHelper(this));
    ActivityExchangeRatesBinding binding;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ExchangeRatesActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exchange_rates);

        binding.recyclerViewCurrencies.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewCurrencies.setAdapter(new CurrenciesAdapter(new ArrayList<Currency>(), this));

        setTitle(getString(R.string.exchange_rates_title));

        if (isOnline()) {
            new GetCurrenciesTaskFromInternet().execute();
        } else {
            new GetCurrenciesTaskFromDatabase().execute();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void showInternetAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.no_internet)
                .setMessage(R.string.no_internet_message)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        ExchangeRatesActivity.this.finish();
                    }
                }).show();
    }

    private void showOldDataAlertDialog(final String updateDate) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.no_internet)
                .setMessage(getString(R.string.old_data_message, updateDate))
                .show();
    }

    private class GetCurrenciesTaskFromInternet extends AsyncTask<Void, Void, List<Currency>> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ExchangeRatesActivity.this);
            dialog.setTitle(getString(R.string.downloading_data));
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected List<Currency> doInBackground(Void... params) {
            List<Currency> currencies = null;
            try {
                currencies = new CurrencyService(NbpClient.FactoryNbpClient.getNbpClient()).getAllCurrencies();
                repository.deleteAll();
                repository.add(currencies);
            } catch (IOException e) {
                Log.e("ExchangeRatesActivity", "onClick: ", e);
            }
            return currencies;
        }

        @Override
        protected void onPostExecute(List<Currency> currencies) {
            dialog.dismiss();
            super.onPostExecute(currencies);
            binding.recyclerViewCurrencies.setAdapter(new CurrenciesAdapter(currencies, ExchangeRatesActivity.this));
            binding.recyclerViewCurrencies.invalidate();
        }
    }

    private class GetCurrenciesTaskFromDatabase extends AsyncTask<Void, Void, List<Currency>> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ExchangeRatesActivity.this);
            dialog.setTitle(getString(R.string.downloading_data));
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected List<Currency> doInBackground(Void... params) {
            return repository.getAll();
        }

        @Override
        protected void onPostExecute(List<Currency> currencies) {
            dialog.dismiss();
            if (currencies.isEmpty()) {
                showInternetAlertDialog();
                return;
            }
            super.onPostExecute(currencies);
            binding.recyclerViewCurrencies.setAdapter(new CurrenciesAdapter(currencies, ExchangeRatesActivity.this));
            binding.recyclerViewCurrencies.invalidate();
            showOldDataAlertDialog(getUpdateDate(currencies));
        }
    }

    private String getUpdateDate(final List<Currency> currencies) {
        return currencies.get(0).getDate().toString();
    }
}
