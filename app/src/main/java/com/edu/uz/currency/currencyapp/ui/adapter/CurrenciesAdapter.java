package com.edu.uz.currency.currencyapp.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edu.uz.currency.currencyapp.R;
import com.edu.uz.currency.currencyapp.databinding.RowCurrencyBinding;
import com.edu.uz.currency.currencyapp.model.Currency;

import java.util.List;


public class CurrenciesAdapter extends RecyclerView.Adapter<CurrenciesAdapter.ViewHolder> {

    private List<Currency> currencies;
    private Context context;

    public CurrenciesAdapter(List<Currency> currencies, Context context) {
        this.currencies = currencies;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowCurrencyBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_currency, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setCurrency(currencies.get(position));
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        RowCurrencyBinding binding;

        public ViewHolder(final RowCurrencyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
