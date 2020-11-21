package com.integrity.feshar.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.integrity.feshar.R;
import com.integrity.feshar.adapters.WatchListAdapter;
import com.integrity.feshar.databinding.ActivityWatchListBinding;
import com.integrity.feshar.models.TvShow;
import com.integrity.feshar.utilities.Constants;
import com.integrity.feshar.viewmodels.WatchListViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.integrity.feshar.utilities.Constants.IS_WATCHLIST_UPDATED;
import static com.integrity.feshar.utilities.Constants.TV_SHOW_KEY;

public class WatchListActivity extends AppCompatActivity implements WatchListAdapter.WatchListListener {

    private ActivityWatchListBinding binding;
    private WatchListViewModel viewModel;
    private WatchListAdapter adapter;
    private List<TvShow> tvShowList;

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
        tvShowList = new ArrayList<>();
        loadWatchList();
    }

    private void loadWatchList(){
        binding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                viewModel.loadWatchList().subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tvShows ->{
                            binding.setIsLoading(false);
                            if (tvShows.size() > 0){
                                tvShowList.clear();
                            }
                            tvShowList.addAll(tvShows);
                            adapter = new WatchListAdapter(this, tvShowList, this);
                            binding.recyclerViewWatchList.setAdapter(adapter);
                            binding.recyclerViewWatchList.setVisibility(View.VISIBLE);
                            compositeDisposable.dispose();
                        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constants.IS_WATCHLIST_UPDATED) {
            loadWatchList();
            IS_WATCHLIST_UPDATED = false;
        }

    }

    @Override
    public void onTvShowClick(TvShow tvShow) {
        Intent i = new Intent(WatchListActivity.this, TvShowDetailsActivity.class);
        i.putExtra(TV_SHOW_KEY, tvShow);
        startActivity(i);
    }

    @Override
    public void removeTvShowFromWatchList(TvShow tvShow, int position) {
        CompositeDisposable disposableDelete = new CompositeDisposable();
        disposableDelete.add(viewModel.removeFromWatchList(tvShow)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    tvShowList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                    disposableDelete.dispose();
                }));
    }
}