
package com.edu.uz.currency.currencyapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TableRate {

    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("mid")
    @Expose
    private double mid;

    public TableRate(final String currency, final String code, final double mid) {
        this.currency = currency;
        this.code = code;
        this.mid = mid;
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

    public double getMid() {
        return mid;
    }

    public void setMid(final double mid) {
        this.mid = mid;
    }
}