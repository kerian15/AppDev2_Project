package com.example.reminder_project;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {

    String BASE_URL = "https://type.fit/api/";

    @GET("quotes")
    Call<List<Quote>> getQuotes();
}
