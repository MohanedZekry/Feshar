package com.integrity.feshar.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.integrity.feshar.repositories.MostPopularTvShowRepository;
import com.integrity.feshar.response.TvShowResponse;

public class MostPopularTvShowsViewModel extends ViewModel {

    private MostPopularTvShowRepository repository;

    public void init(Context context){
    }

    public MostPopularTvShowsViewModel() {
        repository = MostPopularTvShowRepository.getInstance();
    }
    public LiveData<TvShowResponse> getMostPopularMovieTvShows(int page){
        return repository.getMostPopularTvShows(page);
    }

}
