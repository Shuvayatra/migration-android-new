package com.taf.shuvayatra.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.databinding.BlockListItemDataBinding;
import com.taf.shuvayatra.databinding.BlockSliderItemDataBinding;

import java.util.List;

/**
 * Created by julian on 10/18/16.
 */

public class BlockItemAdapter extends RecyclerView.Adapter<BlockItemAdapter.ViewHolder> {

    final List<Post> mItems;
    final LayoutInflater mInflater;
    final int mOrientation;

    public BlockItemAdapter(Context context, List<Post> items, int orientation) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mItems = items;
        this.mOrientation = orientation;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder;
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            BlockListItemDataBinding binding = DataBindingUtil.inflate(mInflater,
                    R.layout.view_list_block_item, parent, false);
            holder = new ViewHolder<>(binding);
        } else {
            BlockSliderItemDataBinding binding = DataBindingUtil.inflate(mInflater,
                    R.layout.view_slider_block_item, parent, false);
            holder = new ViewHolder<>(binding);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            ((BlockItemAdapter.ViewHolder<BlockListItemDataBinding>) holder).mBinding
                    .setPost(mItems.get(position));
        } else if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            ((BlockItemAdapter.ViewHolder<BlockSliderItemDataBinding>) holder).mBinding
                    .setPost(mItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    public class ViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
        public T mBinding;

        public ViewHolder(T binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
