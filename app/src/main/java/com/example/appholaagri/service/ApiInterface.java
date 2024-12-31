package com.example.appholaagri;
import com.example.appholaagri.model.LoginRequest;
import com.example.appholaagri.model.LoginResponse;
import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Body;
public interface ApiInterface {
    @POST("public/login") // Replace with your login endpoint
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
