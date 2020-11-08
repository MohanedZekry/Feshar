package com.integrity.feshar.viewmodels;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.integrity.feshar.repositories.TvShowDetailsRepository;
import com.integrity.feshar.response.TvShowDetailsResponse;

public class TvShowDetailsViewModel extends ViewModel {

    private TvShowDetailsRepository repository;

    public void init(Context context){
    }

    public TvShowDetailsViewModel() {
        repository = repository.getInstance();
    }

    public LiveData<TvShowDetailsResponse> getTvShowsDetails(String tvShowId){
        return repository.getTvShowsDetails(tvShowId);
    }
}
