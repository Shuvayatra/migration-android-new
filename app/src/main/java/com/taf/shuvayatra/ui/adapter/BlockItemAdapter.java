package com.taf.shuvayatra.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taf.data.utils.Logger;
import com.taf.model.Block;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.databinding.BlockListItemDataBinding;
import com.taf.shuvayatra.databinding.BlockSliderItemDataBinding;
import com.taf.shuvayatra.ui.activity.ArticleDetailActivity;
import com.taf.shuvayatra.ui.activity.AudioDetailActivity;
import com.taf.shuvayatra.ui.activity.PlaceDetailActivity;
import com.taf.shuvayatra.ui.activity.VideoDetailActivity;
import com.taf.shuvayatra.ui.interfaces.BlockItemAnalytics;
import com.taf.util.MyConstants;

import java.util.List;

/**
 * Created by julian on 10/18/16.
 */

public class BlockItemAdapter extends RecyclerView.Adapter<BlockItemAdapter.ViewHolder> {

    private static final String TAG = "BlockItemAdapter";
    final LayoutInflater mInflater;
    final int mOrientation;
    List<Post> mItems;
    private Context mContext;
    private Block mParentBlock;
    private BlockItemAnalytics mCallback;

    public BlockItemAdapter(Context context, Block parentBlock,
                            BlockItemAnalytics blockItemAnalytics,
                            List<Post> items, int orientation) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mItems = items;
        this.mOrientation = orientation;
        this.mParentBlock = parentBlock;
        this.mCallback = blockItemAnalytics;
    }

    public void setItems(List<Post> items) {
        mItems = items;
        notifyDataSetChanged();
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    public class ViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
        public T mBinding;

        public ViewHolder(final T binding) {
            super(binding.getRoot());
            mBinding = binding;

            // TODO: 11/2/16 refactor code

            if (binding instanceof BlockListItemDataBinding) {
                ((BlockListItemDataBinding) binding).mainContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Post post = ((BlockListItemDataBinding) binding).getPost();

                        Intent intent = null;
                        switch (post.getDataType()) {
                            case MyConstants.Adapter.TYPE_AUDIO:
                                intent = new Intent(mContext, AudioDetailActivity.class);
                                break;
                            case MyConstants.Adapter.TYPE_VIDEO:
                                intent = new Intent(mContext, VideoDetailActivity.class);
                                break;
                            case MyConstants.Adapter.TYPE_NEWS:
                            case MyConstants.Adapter.TYPE_TEXT:
                                intent = new Intent(mContext, ArticleDetailActivity.class);
                                break;
                            case MyConstants.Adapter.TYPE_PLACE:
                                intent = new Intent(mContext, PlaceDetailActivity.class);
                                break;
                        }

                        if (intent != null) {
                            intent.putExtra(MyConstants.Extras.KEY_ID, post.getId());
                            mContext.startActivity(intent);
                        }
                    }
                });

            } else if (binding instanceof BlockSliderItemDataBinding) {

                ((BlockSliderItemDataBinding) binding).mainContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Post post = ((BlockSliderItemDataBinding) binding).getPost();

                        Intent intent = null;
                        switch (post.getDataType()) {
                            case MyConstants.Adapter.TYPE_AUDIO:
                                intent = new Intent(mContext, AudioDetailActivity.class);
                                break;
                            case MyConstants.Adapter.TYPE_VIDEO:
                                intent = new Intent(mContext, VideoDetailActivity.class);
                                break;
                            case MyConstants.Adapter.TYPE_NEWS:
                            case MyConstants.Adapter.TYPE_TEXT:
                                intent = new Intent(mContext, ArticleDetailActivity.class);
                                break;
                        }

                        if (intent != null) {
                            intent.putExtra(MyConstants.Extras.KEY_ID, post.getId());
                            mContext.startActivity(intent);
                        }
                    }
                });
            }

        }
    }
}
