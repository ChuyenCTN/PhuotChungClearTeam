package com.clearteam.phuotnhom.network.api;

import com.clearteam.phuotnhom.ui.map.model.PlaceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("api/place/nearbysearch/json")
    Call<PlaceResponse> getPlaceNearby(@Query("location") String location, @Query("radius") String radius, @Query("types") String type, @Query("name") String name, @Query("key") String key);


}
