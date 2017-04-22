package com.edu.uz.currency.currencyapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.edu.uz.currency.currencyapp.R;
import com.edu.uz.currency.currencyapp.ui.AtmMapActivity;
import com.edu.uz.currency.currencyapp.databinding.ActivityMainBinding;

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
            public void onClick(final View v) {
                AtmMapActivity.start(MainActivity.this);
            }
        });
    }
}
