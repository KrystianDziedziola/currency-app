package com.edu.uz.currency.currencyapp.ui.chartElements;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.joda.time.LocalDate;

import java.util.List;


public class ChartXAxisValueFormatter implements IAxisValueFormatter {

    private List<LocalDate> mDates;

    public ChartXAxisValueFormatter(List<LocalDate> dates) {
        mDates = dates;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mDates.get((int) value).toString("MM-YY");
    }
}
