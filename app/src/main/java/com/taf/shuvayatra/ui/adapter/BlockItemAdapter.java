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
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.databinding.BlockListItemDataBinding;
import com.taf.shuvayatra.databinding.BlockSliderItemDataBinding;
import com.taf.shuvayatra.ui.activity.ArticleDetailActivity;
import com.taf.shuvayatra.ui.activity.AudioDetailActivity;
import com.taf.shuvayatra.ui.activity.VideoDetailActivity;
import com.taf.util.MyConstants;

import java.util.List;

/**
 * Created by julian on 10/18/16.
 */

public class BlockItemAdapter extends RecyclerView.Adapter<BlockItemAdapter.ViewHolder> {

    final List<Post> mItems;
    final LayoutInflater mInflater;
    final int mOrientation;
    private Context mContext;

    private static final String TAG = "BlockItemAdapter";

    public BlockItemAdapter(Context context, List<Post> items, int orientation) {
        mContext = context;
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

        public ViewHolder(final T binding) {
            super(binding.getRoot());
            mBinding = binding;

            // TODO: 11/2/16 refactor code

            if (binding instanceof BlockListItemDataBinding) {
                Logger.e(TAG, ">>> view and post initialized [item list]");
                ((BlockListItemDataBinding) binding).mainContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Post post = ((BlockListItemDataBinding) binding).getPost();
                        Logger.e(TAG, ">>> selected post: " + post);

                        Intent intent = null;
                        switch (post.getDataType()) {
                            case MyConstants.Adapter.TYPE_AUDIO:
                                Logger.e(TAG, ">>> audio type");
                                intent = new Intent(mContext, AudioDetailActivity.class);
                                break;
                            case MyConstants.Adapter.TYPE_VIDEO:
                                Logger.e(TAG, ">>> video type");
                                intent = new Intent(mContext, VideoDetailActivity.class);
                                break;
                            case MyConstants.Adapter.TYPE_NEWS:
                            case MyConstants.Adapter.TYPE_TEXT:
                                Logger.e(TAG, ">>> news|text type");
                                intent = new Intent(mContext, ArticleDetailActivity.class);
                                break;
                        }

                        if (intent != null) {
                            Logger.e(TAG, ">>> intent not null, start activity");
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
                        Logger.e(TAG, ">>> selected post: " + post);

                        Intent intent = null;
                        switch (post.getDataType()) {
                            case MyConstants.Adapter.TYPE_AUDIO:
                                Logger.e(TAG, ">>> audio type");
                                intent = new Intent(mContext, AudioDetailActivity.class);
                                break;
                            case MyConstants.Adapter.TYPE_VIDEO:
                                Logger.e(TAG, ">>> video type");
                                intent = new Intent(mContext, VideoDetailActivity.class);
                                break;
                            case MyConstants.Adapter.TYPE_NEWS:
                            case MyConstants.Adapter.TYPE_TEXT:
                                Logger.e(TAG, ">>> news|text type");
                                intent = new Intent(mContext, ArticleDetailActivity.class);
                                break;
                        }

                        if (intent != null) {
                            Logger.e(TAG, ">>> intent not null, start activity");
                            intent.putExtra(MyConstants.Extras.KEY_ID, post.getId());
                            mContext.startActivity(intent);
                        }
                    }
                });
            }

        }
    }
}
