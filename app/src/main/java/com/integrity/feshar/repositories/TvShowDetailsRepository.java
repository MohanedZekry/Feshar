package com.integrity.feshar.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.integrity.feshar.network.ApiClient;
import com.integrity.feshar.response.TvShowDetailsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowDetailsRepository implements ITvShowDetailsRepository{

    private static TvShowDetailsRepository instance;

    public synchronized static TvShowDetailsRepository getInstance() {
        if (instance == null)
            instance = new TvShowDetailsRepository();

        return instance;
    }

    @Override
    public LiveData<TvShowDetailsResponse> getTvShowsDetails(String tvShowId) {
        MutableLiveData<TvShowDetailsResponse> data = new MutableLiveData<>();
        ApiClient.getInstance().getTvShowsDetails(tvShowId).enqueue(new Callback<TvShowDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowDetailsResponse> call, @NonNull Response<TvShowDetailsResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TvShowDetailsResponse> call,@NonNull Throwable t) {

            }
        });
        return data;
    }
}
