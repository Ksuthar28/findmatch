package com.saadi.findmatch.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paginate.recycler.LoadingListItemCreator;
import com.saadi.findmatch.R;

/**
 * Created by Kailash Suthar.
 */

public class CustomLoadingListItemCreator implements LoadingListItemCreator {

    private RecyclerView recycleView;

    public CustomLoadingListItemCreator(RecyclerView view) {
        this.recycleView = view;
    }

    private static class VH extends RecyclerView.ViewHolder {

        private VH(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new VH(inflater.inflate(R.layout.custom_loading_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VH vh = (VH) holder;
        // This is how you can make full span if you are using StaggeredGridLayoutManager
        if (recycleView.getLayoutManager() instanceof LinearLayoutManager) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) vh.itemView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
        }
    }
}

