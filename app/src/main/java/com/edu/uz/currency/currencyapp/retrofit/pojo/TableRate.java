
package com.edu.uz.currency.currencyapp.retrofit.pojo;

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