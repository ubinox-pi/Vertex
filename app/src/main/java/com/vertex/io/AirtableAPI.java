package com.vertex.io;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AirtableAPI {

    @Headers({
            "Authorization: Bearer YOUR_API_KEY",
            "Content-Type: application/json"
    })
    @POST("https://api.airtable.com/v0/appOOtw5HApriW4K8/Users") // Replace with your Base ID and Table Name
    Call<AirtableResponse> createRecord(@Body AirtableRecord record);
}

