package com.integrity.feshar.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.integrity.feshar.R;
import com.integrity.feshar.adapters.TvShowsAdapter;
import com.integrity.feshar.databinding.ActivityMainBinding;
import com.integrity.feshar.models.TvShow;
import com.integrity.feshar.response.TvShowResponse;
import com.integrity.feshar.viewmodels.MostPopularTvShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MostPopularTvShowsViewModel viewModel;
    private ActivityMainBinding activityMainBinding;
    private List<TvShow> list;
    private TvShowsAdapter tvShowsAdapter;

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
        tvShowsAdapter = new TvShowsAdapter(this, list);
        activityMainBinding.recyclerViewTvShows.setAdapter(tvShowsAdapter);
        getMostPopularTvShows();
    }

    private void getMostPopularTvShows(){
        activityMainBinding.setIsLoading(true);
        viewModel.getMostPopularMovieTvShows(0).observe(this, tvShowResponse ->  {
            activityMainBinding.setIsLoading(false);
            if (tvShowResponse != null)
            {
                if (tvShowResponse.getTvShows() != null){
                    list.addAll(tvShowResponse.getTvShows());
                    tvShowsAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}