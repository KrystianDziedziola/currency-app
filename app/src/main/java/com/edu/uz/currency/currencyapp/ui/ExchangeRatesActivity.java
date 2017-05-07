package com.edu.uz.currency.currencyapp.ui;

import android.app.Activity;
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
import com.edu.uz.currency.currencyapp.rest.NbpClient;
import com.edu.uz.currency.currencyapp.service.CurrencyService;
import com.edu.uz.currency.currencyapp.ui.adapter.CurrenciesAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesActivity extends AppCompatActivity {

    ActivityExchangeRatesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exchange_rates);

        binding.recyclerViewCurrencies.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewCurrencies.setAdapter(new CurrenciesAdapter(new ArrayList<Currency>(), this));

        if (isOnline()) {
            new GetCurrenciesTask().execute();
        } else {
            // TODO: wczytac dane z bazy

            // TODO: jesli nie ma danych w bazie:
            new AlertDialog.Builder(this)
                    .setTitle(R.string.internet)
                    .setMessage(R.string.no_internet_message)
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            ExchangeRatesActivity.this.finish();
                        }
                    }).show();
        }
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ExchangeRatesActivity.class);
        activity.startActivity(intent);
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    class GetCurrenciesTask extends AsyncTask<Void, Void, List<Currency>> {
        @Override
        protected List<Currency> doInBackground(Void... params) {
            List<Currency> currencies = null;
            try {
                currencies = new CurrencyService(NbpClient.FactoryNbpClient.getNbpClient()).getAllCurrencies();
            } catch (IOException e) {
                Log.e("ExchangeRatesActivity", "onClick: ", e);
            }
            return currencies;
        }

        @Override
        protected void onPostExecute(List<Currency> currencies) {
            super.onPostExecute(currencies);
            binding.recyclerViewCurrencies.setAdapter(new CurrenciesAdapter(currencies, ExchangeRatesActivity.this));
            binding.recyclerViewCurrencies.invalidate();
        }
    }
}
