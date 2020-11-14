package com.integrity.feshar.viewmodels;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.integrity.feshar.database.TvShowDatabase;
import com.integrity.feshar.models.TvShow;
import com.integrity.feshar.repositories.TvShowDetailsRepository;
import com.integrity.feshar.response.TvShowDetailsResponse;
import io.reactivex.Completable;

public class TvShowDetailsViewModel extends ViewModel {

    private TvShowDetailsRepository repository;
    private TvShowDatabase database;
    private Context mContext;

    public void init(Context context) {
        this.mContext = context;
        database = TvShowDatabase.getInstance(mContext);

    }

    public TvShowDetailsViewModel() {
        repository = repository.getInstance();
    }

    public LiveData<TvShowDetailsResponse> getTvShowsDetails(String tvShowId){
        return repository.getTvShowsDetails(tvShowId);
    }

    public Completable addToWatchList(TvShow tvShow){
        return database.tvShowDao().addToWatchList(tvShow);
    }
}
