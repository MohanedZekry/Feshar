package com.integrity.feshar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.integrity.feshar.R;
import com.integrity.feshar.databinding.ItemContainerBinding;
import com.integrity.feshar.models.TvShow;

import java.util.List;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder>{

    private List<TvShow> list;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private WatchListListener watchListListener;

    public WatchListAdapter(Context mContext,List<TvShow> list, WatchListListener watchListListener) {
        this.list = list;
        this.mContext = mContext;
        this.watchListListener = watchListListener;
    }

    class WatchListViewHolder extends RecyclerView.ViewHolder{

        private ItemContainerBinding itemContainerBinding;

        public WatchListViewHolder(ItemContainerBinding itemContainerBinding) {
            super(itemContainerBinding.getRoot());
            this.itemContainerBinding = itemContainerBinding;
        }

        public void bindTvShow(TvShow tvShow){
            itemContainerBinding.setTvShow(tvShow);
            itemContainerBinding.executePendingBindings();
            itemContainerBinding.getRoot().setOnClickListener(view -> { watchListListener.onTvShowClick(tvShow); });
            itemContainerBinding.ivDelete.setOnClickListener(view -> { watchListListener.removeTvShowFromWatchList(tvShow, getAdapterPosition());});
            itemContainerBinding.ivDelete.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public WatchListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerBinding itemContainerBinding = DataBindingUtil.inflate(
                layoutInflater , R.layout.item_container, parent , false
        );
        return new WatchListViewHolder(itemContainerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchListViewHolder holder, int position) {
        holder.bindTvShow(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface WatchListListener {
        void onTvShowClick(TvShow tvShow);
        void removeTvShowFromWatchList(TvShow tvShow, int position);
    }

}
