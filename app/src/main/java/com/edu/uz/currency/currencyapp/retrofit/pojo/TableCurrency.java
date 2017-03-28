
package com.edu.uz.currency.currencyapp.retrofit.pojo;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TableCurrency {

    @SerializedName("table")
    @Expose
    private String table;
    @SerializedName("no")
    @Expose
    private String no;
    @SerializedName("effectiveDate")
    @Expose
    private String effectiveDate;
    @SerializedName("rates")
    @Expose
    private List<TableRate> tableRates = null;

    public String getTable() {
        return table;
    }

    public void setTable(final String table) {
        this.table = table;
    }

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

    public List<TableRate> getRates() {
        return tableRates;
    }

    public void setRates(final List<TableRate> rates) {
        this.tableRates = rates;
    }
}