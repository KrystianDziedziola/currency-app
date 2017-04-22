package com.edu.uz.currency.currencyapp.rest.atm;

import com.edu.uz.currency.currencyapp.rest.atm.model.MainResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleClient {

    String BASE_URL = "https://maps.googleapis.com/maps/";

    @GET("api/place/textsearch/json?key=AIzaSyCCte9Xb-FwvaeLgidHKb-JtEU8ZY94anE")
    Call<MainResponse> getNearbyPlaces(@Query("query") final String type,
                                       @Query("location") final String location,
                                       @Query("radius") final int radius);

    class FactoryGoogleClient {

        private static GoogleClient googleClient;

        private FactoryGoogleClient() {
        }

        public static synchronized GoogleClient getGoogleClient() {
            if (googleClient == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                googleClient = retrofit.create(GoogleClient.class);
            }
            return googleClient;
        }
    }
}
