package com.edu.uz.currency.currencyapp.rest;

import com.edu.uz.currency.currencyapp.rest.model.SingleCurrency;
import com.edu.uz.currency.currencyapp.rest.model.TableCurrency;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NbpClient {

    String BASE_URL = "http://api.nbp.pl/api/exchangerates/";

    @GET("tables/{table}/")
    Call<List<TableCurrency>> getAllCurrencyFromTable(@Path("table") final String table);

    @GET("rates/{table}/{code}/")
    Call<SingleCurrency> getSingleCurrency(@Path("table") final String table,
                                           @Path("code") final String currency);

    @GET("rates/{table}/{code}/{startDate}/{endDate}/")
    Call<SingleCurrency> getSingleCurrencyHistory(@Path("table") final String table,
                                                  @Path("code") final String currency,
                                                  @Path("startDate") final String startDate,
                                                  @Path("endDate") final String endDate);

    class FactoryNbpClient {

        private static NbpClient nbpClient;

        private FactoryNbpClient() {
        }

        public static synchronized NbpClient getNbpClient() {
            if (nbpClient == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                nbpClient = retrofit.create(NbpClient.class);
            }
            return nbpClient;
        }
    }
}