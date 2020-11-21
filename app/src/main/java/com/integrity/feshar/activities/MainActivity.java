package com.integrity.feshar.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import com.integrity.feshar.R;
import com.integrity.feshar.adapters.TvShowsAdapter;
import com.integrity.feshar.databinding.ActivityMainBinding;
import com.integrity.feshar.models.TvShow;
import com.integrity.feshar.viewmodels.MostPopularTvShowsViewModel;
import java.util.ArrayList;
import java.util.List;

import static com.integrity.feshar.utilities.Constants.TV_SHOW_KEY;

public class MainActivity extends AppCompatActivity implements TvShowsAdapter.OnTvShowClickListener {

    private MostPopularTvShowsViewModel viewModel;
    private ActivityMainBinding activityMainBinding;
    private List<TvShow> list;
    private TvShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewInitialization();
    }

    private void viewInitialization(){
        activityMainBinding.recyclerViewTvShows.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTvShowsViewModel.class);
        list = new ArrayList<>();
        tvShowsAdapter = new TvShowsAdapter(this, list, this);
        activityMainBinding.recyclerViewTvShows.setAdapter(tvShowsAdapter);
        activityMainBinding.recyclerViewTvShows.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!activityMainBinding.recyclerViewTvShows.canScrollVertically(1)){
                    if (currentPage <= totalAvailablePages){
                        currentPage += 1;
                        getMostPopularTvShows();
                    }
                }
            }
        });
        activityMainBinding.imageWatchList.setOnClickListener(view -> {
            startActivity(new Intent(this, WatchListActivity.class));
        });
        getMostPopularTvShows();
    }

    private void getMostPopularTvShows(){
        toggleLoading();
        viewModel.getMostPopularMovieTvShows(currentPage).observe(this, tvShowResponse ->  {
            toggleLoading();
            if (tvShowResponse != null)
                totalAvailablePages = tvShowResponse.getTotalPages();
            {
                if (tvShowResponse.getTvShows() != null){
                    int oldCount = list.size();
                    list.addAll(tvShowResponse.getTvShows());
                    tvShowsAdapter.notifyDataSetChanged();
                    tvShowsAdapter.notifyItemRangeInserted(oldCount , list.size());
                }
            }
        });
    }

    private void toggleLoading(){
        if (currentPage == 1){
            if (activityMainBinding.getIsLoading() != null && activityMainBinding.getIsLoading()){
                activityMainBinding.setIsLoading(false);
            }else{
                activityMainBinding.setIsLoading(true);
            }
        }else {
            if (activityMainBinding.getIsLoadingMore() != null && activityMainBinding.getIsLoadingMore()){
                activityMainBinding.setIsLoadingMore(false);
            }else
            {
                activityMainBinding.setIsLoadingMore(true);
            }
        }
    }

    @Override
    public void onTvShowClick(TvShow tvShow) {
        Intent i = new Intent(getApplicationContext(), TvShowDetailsActivity.class);
        i.putExtra(TV_SHOW_KEY, tvShow);
        startActivity(i);
    }
}