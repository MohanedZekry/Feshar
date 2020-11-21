package com.integrity.feshar.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.integrity.feshar.R;
import com.integrity.feshar.adapters.EpisodesAdapter;
import com.integrity.feshar.adapters.ImageSliderAdapter;
import com.integrity.feshar.databinding.ActivityTvShowDetailsBinding;
import com.integrity.feshar.databinding.LayoutEpisodesBottomSheetBinding;
import com.integrity.feshar.models.TvShow;
import com.integrity.feshar.utilities.Constants;
import com.integrity.feshar.viewmodels.TvShowDetailsViewModel;
import java.util.Locale;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.integrity.feshar.utilities.Constants.TV_SHOW_KEY;

public class TvShowDetailsActivity extends AppCompatActivity {

    private ActivityTvShowDetailsBinding activityTvShowDetailsBinding;
    private TvShowDetailsViewModel viewModel;
    private BottomSheetDialog bottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;
    private TvShow tvShow;
    private Boolean isTvShowAvailableInWatchlist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTvShowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tv_show_details);
        viewInitialization();
    }

    /* INITIALIZE LAYOUT VIEWS */
    private void viewInitialization(){
        viewModel = new ViewModelProvider(this).get(TvShowDetailsViewModel.class);
        viewModel.init(TvShowDetailsActivity.this);
        activityTvShowDetailsBinding.ivBack.setOnClickListener(view -> { onBackPressed(); });
        tvShow = (TvShow) getIntent().getSerializableExtra(TV_SHOW_KEY);
        checkTvShowInWatchlist();
        getTvShowDetails();

    }

    private void checkTvShowInWatchlist(){
        CompositeDisposable disposableCheck = new CompositeDisposable();
        disposableCheck.add(
                viewModel.getTvShowFromWatchlist(String.valueOf(tvShow.getId()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShow -> {
                    isTvShowAvailableInWatchlist = true;
                    activityTvShowDetailsBinding.ivAddToWatchList.setImageResource(R.drawable.ic_add);
                    disposableCheck.dispose();
                })
        );
    }

    /* RETRIEVE TV-SHOW DETAILS FROM API AND LOAD INTO VIEWS */
    private void getTvShowDetails(){
        activityTvShowDetailsBinding.setIsLoading(true);

        String tvShowId = String.valueOf(tvShow.getId());

        /* GET DATA FROM API */
        viewModel.getTvShowsDetails(tvShowId).observe(this, tvShowDetailsResponse -> {
            activityTvShowDetailsBinding.setIsLoading(false);
            /* CHECK IF THE DATA IS NULL */
            if (tvShowDetailsResponse.getTvShowDetails() != null){
                if (tvShowDetailsResponse.getTvShowDetails().getPictures() != null){
                    /* LOAD IMAGES TO THE SLIDER */
                    loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                }

                activityTvShowDetailsBinding.setTvShowImageURL(
                        tvShowDetailsResponse.getTvShowDetails().getImagePath());
                makeViewVisible(activityTvShowDetailsBinding.ivTvShow);

                /* LOAD THE DESCRIPTION */
                activityTvShowDetailsBinding.setDescription(
                        String.valueOf(HtmlCompat.fromHtml(
                                tvShowDetailsResponse.getTvShowDetails().getDescription(),
                                HtmlCompat.FROM_HTML_MODE_LEGACY
                        ))
                );
                makeViewVisible(activityTvShowDetailsBinding.tvDescription);
                makeViewVisible(activityTvShowDetailsBinding.tvReadMore);

                /* DOING READ MORE DESCRIPTION */
                activityTvShowDetailsBinding.tvReadMore.setOnClickListener(view -> {
                    if (activityTvShowDetailsBinding.tvReadMore.getText().equals(getString(R.string.read_more))){
                        activityTvShowDetailsBinding.tvDescription.setMaxLines(Integer.MAX_VALUE);
                        activityTvShowDetailsBinding.tvDescription.setEllipsize(null);
                        activityTvShowDetailsBinding.tvReadMore.setText(getString(R.string.read_less));
                    }else{
                        activityTvShowDetailsBinding.tvDescription.setMaxLines(4);
                        activityTvShowDetailsBinding.tvDescription.setEllipsize(TextUtils.TruncateAt.END);
                        activityTvShowDetailsBinding.tvReadMore.setText(getString(R.string.read_more));
                    }
                });

                /* DOING RATING */
                activityTvShowDetailsBinding.setRating(
                        String.format(
                                Locale.getDefault(),
                                "%.2f",
                                Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating())
                        )
                );

                /* CHECK IF THE GENRE IS NOT NULL */
                if (tvShowDetailsResponse.getTvShowDetails().getGenres() != null){
                    activityTvShowDetailsBinding.setGenre(tvShowDetailsResponse.getTvShowDetails().getGenres()[0]);
                }else{
                    activityTvShowDetailsBinding.setGenre("N/A");
                }

                activityTvShowDetailsBinding.setRuntime(tvShowDetailsResponse.getTvShowDetails().getRuntime() + " Min");
                makeViewVisible(activityTvShowDetailsBinding.viewDivider);
                makeViewVisible(activityTvShowDetailsBinding.viewDivider2);
                makeViewVisible(activityTvShowDetailsBinding.layoutMisc);

                activityTvShowDetailsBinding.btnWebsite.setOnClickListener(view -> {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                    startActivity(i);
                });
                makeViewVisible(activityTvShowDetailsBinding.btnWebsite);
                makeViewVisible(activityTvShowDetailsBinding.btnEpisode);

                activityTvShowDetailsBinding.btnEpisode.setOnClickListener(view -> {
                    if (bottomSheetDialog == null){
                        bottomSheetDialog = new BottomSheetDialog(TvShowDetailsActivity.this);
                        layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                                LayoutInflater.from(TvShowDetailsActivity.this),
                                R.layout.layout_episodes_bottom_sheet,
                                findViewById(R.id.containerEpisodes),
                                false
                        );
                        bottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());
                        layoutEpisodesBottomSheetBinding.recyclerViewEpisodes.setAdapter(
                                new EpisodesAdapter(this, tvShowDetailsResponse.getTvShowDetails().getEpisodes())
                        );
                        layoutEpisodesBottomSheetBinding.tvTitle.setText(
                                String.format("Episode | %s", tvShow.getName())
                        );

                        layoutEpisodesBottomSheetBinding.ivClose.setOnClickListener(view1 -> { bottomSheetDialog.dismiss(); });

                    }
                    // -- Optional Section Start --\\
                    FrameLayout frameLayout = bottomSheetDialog.findViewById(
                        com.google.android.material.R.id.design_bottom_sheet
                    );
                    if (frameLayout != null){
                        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                        bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    bottomSheetDialog.show();
                });

                activityTvShowDetailsBinding.ivAddToWatchList.setOnClickListener(add ->
                {
                    CompositeDisposable disposable = new CompositeDisposable();
                    if (isTvShowAvailableInWatchlist){

                        disposable.add(viewModel.removeFromWatchList(tvShow)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            isTvShowAvailableInWatchlist = false;
                            Constants.IS_WATCHLIST_UPDATED = true;
                            activityTvShowDetailsBinding.ivAddToWatchList.setImageResource(R.drawable.ic_eye);
                            Toast.makeText(getApplicationContext(), "Remove from watchlist", Toast.LENGTH_SHORT).show();
                            disposable.dispose();
                        }));

                    }else{
                        disposable.add(viewModel.addToWatchList(tvShow)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(()-> {
                                    Constants.IS_WATCHLIST_UPDATED = true;
                                    activityTvShowDetailsBinding.ivAddToWatchList.setImageResource(R.drawable.ic_add);
                                    Toast.makeText(getApplicationContext(), "Added to watchlist", Toast.LENGTH_SHORT).show();
                                    disposable.dispose();
                                }));
                    }
                });

                makeViewVisible(activityTvShowDetailsBinding.ivAddToWatchList);

                /* PASSING THE DATA TO VIEWS*/
                loadTvShowDetails();
            }
        });
    }

    /* SETUP IMAGE SLIDER */
    private void loadImageSlider(String[] sliderImages){
        activityTvShowDetailsBinding.viewPagerSlider.setOffscreenPageLimit(1);
        activityTvShowDetailsBinding.viewPagerSlider.setAdapter(new ImageSliderAdapter(getApplicationContext(),sliderImages));
        makeViewVisible(activityTvShowDetailsBinding.viewPagerSlider);
        makeViewVisible(activityTvShowDetailsBinding.viewFadingEdge);
        setUpSliderIndicator(sliderImages.length);
    }

    /* PASSING THE DATA TO THE VIEWS */
    private void loadTvShowDetails(){
        activityTvShowDetailsBinding.setTvShowName(tvShow.getName());
        activityTvShowDetailsBinding.setNetworkCountry(
                tvShow.getNetwork() + " (" + tvShow.getCountry()
                + ")"
        );

        activityTvShowDetailsBinding.setStatus(tvShow.getStatus());
        activityTvShowDetailsBinding.setStartedDate(tvShow.getStartDate());

        makeViewVisible(activityTvShowDetailsBinding.tvTextName);
        makeViewVisible(activityTvShowDetailsBinding.tvNetworkCountry);
        makeViewVisible(activityTvShowDetailsBinding.tvStatus);
        makeViewVisible(activityTvShowDetailsBinding.tvStartedDate);
    }

    private void makeViewVisible(View view){
        view.setVisibility(View.VISIBLE);
    }

    /* SETUP THE INDICATOR ON THE SLIDER */
    private void setUpSliderIndicator(int count){
        ImageView[] indicator = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);

        for (int i = 0; i < count; i++){
            indicator[i] = new ImageView(getApplicationContext());
            indicator[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.background_slider_indicator_inactive
            ));
            indicator[i].setLayoutParams(layoutParams);
            activityTvShowDetailsBinding.layoutSliderIndicator.addView(indicator[i]);
        }
        makeViewVisible(activityTvShowDetailsBinding.layoutSliderIndicator);
        setupCurrentSliderIndicator(0);
        activityTvShowDetailsBinding.viewPagerSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setupCurrentSliderIndicator(position);
            }
        });
    }

    /* MANAGE CURRENT SLIDER INDICATOR WITH IMAGE */
    private void setupCurrentSliderIndicator(int position){
        int childCount = activityTvShowDetailsBinding.layoutSliderIndicator.getChildCount();
        for (int i = 0; i < childCount; i++){
            ImageView imageView = (ImageView) activityTvShowDetailsBinding.layoutSliderIndicator.getChildAt(i);
            if (i == position){
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.background_slider_indicator_active
                ));
            }
            else{
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.background_slider_indicator_inactive
                ));
            }
        }
    }

}