package com.example.amc_app.ApiHelper;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiService {

    @GET()
    Single<JsonElement> getCall(@Url String url);

    @GET()
    Single<JsonElement> getCall(@Url String url, @FieldMap() Map<String, String> params);


    @FormUrlEncoded
    @POST()
    Single<JsonElement> postCall(@Url String url,@FieldMap() Map<String, String> params);


    @GET()
    Single<JsonElement> getOtpApi(@Url String url);

   // @Headers("Content-Type: application/json")
    @POST()
    Single<JsonElement> userLogin(@Url String url,@Body Map<String, String> map);


    @POST()
    Single<JsonElement> makeMutlipartCall(@Url String url, @Body RequestBody file);

}
