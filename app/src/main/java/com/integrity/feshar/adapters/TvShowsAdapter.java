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

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.TvShowViewHolder>{

    private List<TvShow> list;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private OnTvShowClickListener onTvShowClickListener;

    public TvShowsAdapter(Context mContext,List<TvShow> list, OnTvShowClickListener onTvShowClickListener) {
        this.list = list;
        this.mContext = mContext;
        this.onTvShowClickListener = onTvShowClickListener;
    }

    class TvShowViewHolder extends RecyclerView.ViewHolder{

        private ItemContainerBinding itemContainerBinding;

        public TvShowViewHolder(ItemContainerBinding itemContainerBinding) {
            super(itemContainerBinding.getRoot());
            this.itemContainerBinding = itemContainerBinding;
        }

        public void bindTvShow(TvShow tvShow){
            itemContainerBinding.setTvShow(tvShow);
            itemContainerBinding.executePendingBindings();
            itemContainerBinding.getRoot().setOnClickListener(view -> { onTvShowClickListener.onTvShowClick(tvShow); });
        }

    }

    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerBinding itemContainerBinding = DataBindingUtil.inflate(
                layoutInflater , R.layout.item_container, parent , false
        );
        return new TvShowViewHolder(itemContainerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowViewHolder holder, int position) {
        holder.bindTvShow(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnTvShowClickListener {
        void onTvShowClick(TvShow tvShow);
    }

}
