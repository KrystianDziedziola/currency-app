package com.edu.uz.currency.currencyapp.ui;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.edu.uz.currency.currencyapp.R;
import com.edu.uz.currency.currencyapp.databinding.ActivityMainBinding;
import com.edu.uz.currency.currencyapp.model.Currency;
import com.edu.uz.currency.currencyapp.rest.CurrencyService;
import com.edu.uz.currency.currencyapp.rest.NbpClient;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.buttonExchangeRates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExchangeRatesActivity.start(MainActivity.this);
            }
        });

        binding.buttonCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Przejście do aktywności z kalkulatorem.
            }
        });

        binding.buttonAtms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Przejście do aktywności z mapa bankomatow.
            }
        });
    }


}
