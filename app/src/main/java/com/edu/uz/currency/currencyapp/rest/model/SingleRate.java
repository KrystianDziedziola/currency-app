
package com.edu.uz.currency.currencyapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleRate {

    @SerializedName("no")
    @Expose
    private String no;
    @SerializedName("effectiveDate")
    @Expose
    private String effectiveDate;
    @SerializedName("mid")
    @Expose
    private double mid;

    public String getNo() {
        return no;
    }

    public void setNo(final String no) {
        this.no = no;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(final String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public double getMid() {
        return mid;
    }

    public void setMid(final double mid) {
        this.mid = mid;
    }
}