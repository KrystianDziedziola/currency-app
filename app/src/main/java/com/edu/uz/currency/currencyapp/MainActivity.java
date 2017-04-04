package com.edu.uz.currency.currencyapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.edu.uz.currency.currencyapp.atm.AtmMapActivity;
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
                // TODO: Przejście do aktywności z Kursami walut.
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
                final Intent atmMaps = new Intent(MainActivity.this, AtmMapActivity.class);
                startActivity(atmMaps);
            }
        });
    }
}
