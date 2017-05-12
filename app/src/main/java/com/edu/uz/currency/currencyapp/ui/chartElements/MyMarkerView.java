package com.edu.uz.currency.currencyapp.ui.chartElements;

import android.content.Context;
import android.widget.TextView;

import com.edu.uz.currency.currencyapp.R;
import com.edu.uz.currency.currencyapp.helper.Constants;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import org.joda.time.LocalDate;

import java.util.List;


public class MyMarkerView extends MarkerView {

    private TextView textView;
    private List<LocalDate> mDates;

    public MyMarkerView(Context context, int layoutResource, List<LocalDate> dates) {
        super(context, layoutResource);
        textView = (TextView) findViewById(R.id.marker_text);
        mDates = dates;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        textView.setText(mDates.get((int) e.getX()).toString(Constants.DATE_FORMAT) + "\n" + Float.toString(e.getY()));

        super.refreshContent(e, highlight);
    }
}
