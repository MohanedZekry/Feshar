package com.integrity.feshar.repositories;

import androidx.lifecycle.LiveData;

public interface IMostPopularTvShowRepository {

    public LiveData getMostPopularTvShows(int page);
}
