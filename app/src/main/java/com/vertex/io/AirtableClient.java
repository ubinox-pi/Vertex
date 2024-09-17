package com.vertex.io;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AirtableClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.airtable.com/v0/") // Airtable API base URL
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

