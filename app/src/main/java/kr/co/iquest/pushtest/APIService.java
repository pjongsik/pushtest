package kr.co.iquest.pushtest;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("/")
    Call<String> getPublicIp();
}
