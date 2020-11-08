package com.integrity.feshar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.integrity.feshar.R;
import com.integrity.feshar.databinding.ItemContainerEpisodeBinding;
import com.integrity.feshar.models.Episodes;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder> {

    private Context mContext;
    private List<Episodes> list;
    private LayoutInflater layoutInflater;

    public EpisodesAdapter(Context mContext, List<Episodes> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerEpisodeBinding itemContainerEpisodeBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_episode, parent, false
        );
        return new EpisodeViewHolder(itemContainerEpisodeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
            holder.bindEpisode(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class EpisodeViewHolder extends RecyclerView.ViewHolder {

        private ItemContainerEpisodeBinding itemContainerEpisodeBinding;

        public EpisodeViewHolder(ItemContainerEpisodeBinding itemContainerEpisodeBinding) {
            super(itemContainerEpisodeBinding.getRoot());
            this.itemContainerEpisodeBinding = itemContainerEpisodeBinding;
        }

        public void bindEpisode(Episodes episodes){
            String title = "S";
            String season = episodes.getSeason();
            if (season.length() == 1){
                season = "0".concat(season);
            }
            String episodeNumber = episodes.getEpisode();
            if (episodeNumber.length() == 1){
                episodeNumber = "0".concat(episodeNumber);
            }
            episodeNumber = "E".concat(episodeNumber);
            title = title.concat(season).concat(episodeNumber);
            itemContainerEpisodeBinding.setTitle(title);
            itemContainerEpisodeBinding.setName(episodes.getName());
            itemContainerEpisodeBinding.setAirDate(episodes.getAirDate());
        }
    }

}
