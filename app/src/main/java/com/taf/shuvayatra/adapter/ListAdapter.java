package com.taf.shuvayatra.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taf.model.BaseModel;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.databinding.ArticleDataBinding;
import com.taf.shuvayatra.databinding.AudioVideoDataBinding;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.util.MyConstants.Adapter;

import java.util.List;

import butterknife.ButterKnife;

public class ListAdapter<T extends BaseModel> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    List<T> mDataCollection;
    ListItemClickListener mListener;
    Boolean mIsGrid = false;

    public ListAdapter(Context pContext, ListItemClickListener pListener) {
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        this.mListener = pListener;
    }

    public ListAdapter(Context pContext, List<T> pDataCollection, ListItemClickListener pListener) {
        mDataCollection = pDataCollection;
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        this.mListener = pListener;
    }

    public List<T> getDataCollection() {
        return mDataCollection;
    }

    public void setDataCollection(List<T> pDataCollection) {
        mDataCollection = pDataCollection;
        notifyDataSetChanged();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case Adapter.TYPE_AUDIO:
            case Adapter.TYPE_VIDEO:
                    AudioVideoDataBinding vBinding = DataBindingUtil.inflate(mLayoutInflater, R.layout
                            .view_audio_video_list, parent, false);
                    viewHolder = new AudioVideoViewHolder(vBinding);
                break;
            case Adapter.TYPE_NEWS:
            case Adapter.TYPE_TEXT:
                ArticleDataBinding articleBinding = DataBindingUtil.inflate(mLayoutInflater, R.layout
                        .view_article, parent, false);
                viewHolder = new ArticleViewHolder(articleBinding);
                break;
            default:
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case Adapter.TYPE_AUDIO:
            case Adapter.TYPE_VIDEO:
                ((AudioVideoViewHolder) holder).mBinding.setContent((Post) mDataCollection.get(position));
                break;
            case Adapter.TYPE_NEWS:
            case Adapter.TYPE_TEXT:
                ((ArticleViewHolder) holder).mBinding.setArticle((Post) mDataCollection.get
                        (position));
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataCollection.get(position).getDataType();
    }

    @Override
    public int getItemCount() {
        return (mDataCollection != null) ? mDataCollection.size() : 0;
    }

    public class AudioVideoViewHolder extends RecyclerView.ViewHolder {

        public AudioVideoDataBinding mBinding;

        public AudioVideoViewHolder(AudioVideoDataBinding binding) {
            super(binding.getRoot());
            ButterKnife.bind(this, binding.getRoot());
            this.mBinding = binding;
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onListItemSelected(mBinding.getContent());
                }
            });
        }
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        public ArticleDataBinding mBinding;

        public ArticleViewHolder(ArticleDataBinding binding) {
            super(binding.getRoot());
            ButterKnife.bind(this, binding.getRoot());
            this.mBinding = binding;
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onListItemSelected(mBinding.getArticle());
                }
            });
        }
    }
}