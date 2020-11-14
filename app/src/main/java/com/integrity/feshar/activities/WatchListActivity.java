package com.integrity.feshar.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.integrity.feshar.R;
import com.integrity.feshar.databinding.ActivityWatchListBinding;
import com.integrity.feshar.viewmodels.WatchListViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchListActivity extends AppCompatActivity {

    private ActivityWatchListBinding binding;
    private WatchListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_watch_list);
        viewInitialization();
    }

    private void viewInitialization(){
        viewModel = new ViewModelProvider(this).get(WatchListViewModel.class);
        viewModel.init(this);
        binding.ivBack.setOnClickListener(view -> { onBackPressed(); });
    }

    private void loadWatchList(){
        binding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                viewModel.loadWatchList().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows ->{
                        binding.setIsLoading(false);
                    Toast.makeText(getApplicationContext(), "Watchlist "+ tvShows.size(),Toast.LENGTH_SHORT).show();
                }));

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWatchList();
    }
}