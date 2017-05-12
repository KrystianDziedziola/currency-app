package com.edu.uz.currency.currencyapp.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edu.uz.currency.currencyapp.R;
import com.edu.uz.currency.currencyapp.databinding.RowCurrencyBinding;
import com.edu.uz.currency.currencyapp.model.Currency;
import com.edu.uz.currency.currencyapp.ui.CurrencyAboutActivity;

import java.util.List;


public class CurrenciesAdapter extends RecyclerView.Adapter<CurrenciesAdapter.ViewHolder> {

    private List<Currency> currencies;
    private Activity activity;

    public CurrenciesAdapter(List<Currency> currencies, Activity activity) {
        this.currencies = currencies;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowCurrencyBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_currency, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.binding.setCurrency(currencies.get(position));
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrencyAboutActivity.start(activity, currencies.get(holder.getAdapterPosition()).getCode());
            }
        });
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RowCurrencyBinding binding;

        public ViewHolder(final RowCurrencyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setOnClickListener(View.OnClickListener listener) {
            binding.getRoot().setOnClickListener(listener);
        }
    }
}
