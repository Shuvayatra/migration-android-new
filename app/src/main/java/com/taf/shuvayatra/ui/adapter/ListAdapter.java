package com.taf.shuvayatra.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.taf.model.BaseModel;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;

import java.util.List;

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
            default:
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
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
}
