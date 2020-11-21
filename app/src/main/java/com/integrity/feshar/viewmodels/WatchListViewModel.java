package com.integrity.feshar.viewmodels;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.integrity.feshar.database.TvShowDatabase;
import com.integrity.feshar.models.TvShow;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class WatchListViewModel extends ViewModel {

    private TvShowDatabase database;
    private Context mContext;

    public void init(Context mContext){
        this.mContext = mContext;
        database = TvShowDatabase.getInstance(mContext);
    }

    public Flowable<List<TvShow>> loadWatchList(){
        return database.tvShowDao().getWatchList();
    }

    public Completable removeFromWatchList(TvShow tvShow){
        return database.tvShowDao().removeFromWatchList(tvShow);
    }
}
