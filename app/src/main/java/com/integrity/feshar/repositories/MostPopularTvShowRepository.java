package com.integrity.feshar.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.integrity.feshar.network.ApiClient;
import com.integrity.feshar.response.TvShowResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPopularTvShowRepository implements IMostPopularTvShowRepository {

    private  static  MostPopularTvShowRepository instance;

    public synchronized static MostPopularTvShowRepository getInstance() {
        if (instance == null)
            instance = new MostPopularTvShowRepository();

        return instance;
    }

    @Override
    public LiveData getMostPopularTvShows(int page) {
        MutableLiveData<TvShowResponse> data = new MutableLiveData<>();
        ApiClient.getInstance().getMostPopularTvShows(page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(Call<TvShowResponse> call, Response<TvShowResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TvShowResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

}
