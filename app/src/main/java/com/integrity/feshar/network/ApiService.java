package com.integrity.feshar.network;

import com.integrity.feshar.response.TvShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<TvShowResponse> getMostPopularTVShows(@Query("page") int page);

}
