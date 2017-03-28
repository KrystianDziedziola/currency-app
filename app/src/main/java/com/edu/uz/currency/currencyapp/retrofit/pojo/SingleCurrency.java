
package com.edu.uz.currency.currencyapp.retrofit.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleCurrency {

    @SerializedName("table")
    @Expose
    private String table;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("rates")
    @Expose
    private List<SingleRate> rates = null;

    public String getTable() {
        return table;
    }

    public void setTable(final String table) {
        this.table = table;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public List<SingleRate> getRates() {
        return rates;
    }

    public void setRates(final List<SingleRate> rates) {
        this.rates = rates;
    }
}