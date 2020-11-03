package com.integrity.feshar.network;

import com.integrity.feshar.response.TvShowResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://www.episodate.com/api/";
    private static ApiClient instance;
    private ApiService apiService;

    public static ApiClient getInstance() {
        if (instance == null)
            instance = new ApiClient();

        return instance;
    }

    private ApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public Call<TvShowResponse> getMostPopularTvShows(int page) {
        return apiService.getMostPopularTVShows(page);
    }

}
