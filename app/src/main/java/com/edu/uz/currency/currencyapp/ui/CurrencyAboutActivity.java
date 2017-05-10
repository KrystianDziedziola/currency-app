package com.edu.uz.currency.currencyapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.edu.uz.currency.currencyapp.R;
import com.edu.uz.currency.currencyapp.databinding.ActivityCurrencyAboutBinding;
import com.edu.uz.currency.currencyapp.ui.chartElements.ChartXAxisValueFormatter;
import com.edu.uz.currency.currencyapp.helper.Constants;
import com.edu.uz.currency.currencyapp.model.Currency;
import com.edu.uz.currency.currencyapp.rest.NbpClient;
import com.edu.uz.currency.currencyapp.service.CurrencyService;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyAboutActivity extends AppCompatActivity {

    private final static String EXTRA_CURRENCY_CODE = "EXTRA_CURRENCY_CODE";
    private String currencyCode;
    private List<Currency> currencyHistory;
    private ActivityCurrencyAboutBinding binding;

    private List<Entry> entries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_currency_about);
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
                DateTime date = new DateTime();
                String endDate = date.toString(Constants.DATE_FORMAT);
                String startDate = date.plusYears(-1).toString(Constants.DATE_FORMAT);
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
            List<LocalDate> dates = new ArrayList<>();
            for (int i = 0; i < currencies.size(); i++) {
                entries.add(new Entry(i, (float) currencies.get(i).getRate()));
                dates.add(currencies.get(i).getDate());
            }
            String chartLabel = currencies.get(0).getCode() + "-PLN";
            LineDataSet dataSet = new LineDataSet(entries, chartLabel);
            LineData lineData = new LineData(dataSet);
            binding.chart.setData(lineData);
            binding.chart.getXAxis().setValueFormatter(new ChartXAxisValueFormatter(dates));
            binding.chart.invalidate();
        }
    }
}
