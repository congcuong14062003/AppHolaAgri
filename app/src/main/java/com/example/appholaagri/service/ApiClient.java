package com.example.appholaagri.service;

import android.content.Context;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://agriapp.imediatech.com.vn/agri-app/api/";
//    private static final String BASE_URL = "https://sb-agri.imediatech.com.vn/agri-app/api/";
    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)  // Tăng thời gian chờ kết nối
                    .readTimeout(120, TimeUnit.SECONDS)     // Tăng thời gian chờ đọc dữ liệu
                    .writeTimeout(120, TimeUnit.SECONDS)    // Tăng thời gian chờ ghi dữ liệu
                    .addInterceptor(new AuthInterceptor(context))
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
