package com.integrity.feshar.repositories;

import androidx.lifecycle.LiveData;
import com.integrity.feshar.response.TvShowDetailsResponse;

public interface ITvShowDetailsRepository {

    public LiveData<TvShowDetailsResponse> getTvShowsDetails(String tvShowId);

}
