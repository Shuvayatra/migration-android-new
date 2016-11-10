package com.taf.shuvayatra.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taf.model.BaseModel;
import com.taf.model.Channel;
import com.taf.model.HeaderItem;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.databinding.ItemChannelHeaderDataBinding;
import com.taf.shuvayatra.databinding.ItemChannelListDataBinding;
import com.taf.shuvayatra.databinding.ItemChannelListSelectedDataBinding;
import com.taf.shuvayatra.ui.activity.PodcastsActivity;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ngima on 11/3/16.
 */

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ViewHolder> {

    private static final String TAG = "ChannelAdapter";



    List<BaseModel> mChannels;
    LayoutInflater mLayoutInflater;
    Context context;

    public ChannelAdapter(Context context) {
        mChannels = new ArrayList<>();
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setmChannels(List<BaseModel> mChannels) {
        this.mChannels = mChannels;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding dataBinding;

        switch (viewType){
            case MyConstants.Adapter.TYPE_CHANNEL:
                dataBinding = DataBindingUtil.inflate(mLayoutInflater, R.layout
                        .item_channel_list, parent, false);
                return new ViewHolder(dataBinding);
            case MyConstants.Adapter.TYPE_CHANNEL_HEADER:
                dataBinding = DataBindingUtil.inflate(mLayoutInflater, R.layout
                        .item_channel_header, parent, false);
                return new ViewHolder((dataBinding));
//            case MyConstants.Adapter.TYPE_CHANNEL_SELECTED:
//                dataBinding = DataBindingUtil.inflate(mLayoutInflater, R.layout
//                        .item_channel_list_selected, parent, false);
//                return new ViewHolder(dataBinding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int type = getItemViewType(position);

        switch (type){
            case MyConstants.Adapter.TYPE_CHANNEL:
                ((ItemChannelListDataBinding)holder.mBinding)
                        .setChannel((Channel) mChannels.get(position));
                break;
            case MyConstants.Adapter.TYPE_CHANNEL_HEADER:
                ((ItemChannelHeaderDataBinding)holder.mBinding)
                        .setTitle(((HeaderItem)mChannels.get(position)).getTitle());
                break;
//            case MyConstants.Adapter.TYPE_CHANNEL_SELECTED:
//                ((ItemChannelListSelectedDataBinding)holder.mBinding)
//                        .setChannel((Channel)mChannels.get(position));
//                break;
        }
    }

    @Override
    public int getItemCount() {
        return mChannels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mChannels.get(position).getDataType();
    }

    public class ViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder{
        T mBinding;
        public ViewHolder(T binding) {
            super(binding.getRoot());

            mBinding = binding;
            if (mBinding instanceof ItemChannelListDataBinding || mBinding instanceof ItemChannelListSelectedDataBinding){
                mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "onClick: Channel with id >>"+mChannels.get(getAdapterPosition())
                                .getId(), null);
                        Channel channel = (Channel) mChannels.get(getAdapterPosition());
                        Intent intent = new Intent(context, PodcastsActivity.class);
                        intent.putExtra(MyConstants.Extras.KEY_ID, channel.getId());
                        intent.putExtra(MyConstants.Extras.KEY_TITLE, channel.getTitle());
                        context.startActivity(intent);
                    }
                });
            }
        }
    }
}
