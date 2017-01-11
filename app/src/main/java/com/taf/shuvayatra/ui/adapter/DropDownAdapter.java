package com.taf.shuvayatra.ui.adapter;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taf.shuvayatra.R;

import java.util.List;

public class DropDownAdapter extends BaseAdapter {

    private List<String> mData;
    private LayoutInflater mLayoutInflater;
    protected Context mContext;

    public DropDownAdapter(Context context, List<String> data) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mData = data;
    }

    public void setData(List<String> mData) {
        this.mData = mData;
    }

    public List<String> getData() {
        return mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(parent, position, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(parent, position, true);
    }

    public View getCustomView(ViewGroup parent, int position, boolean dropdown) {
        View spinner = mLayoutInflater.inflate(R.layout.view_dropdown, parent, false);
        TextView textView = (TextView) spinner.findViewById(R.id.filterText);
        textView.setText(getSpinnerText(position));

        ImageView imageView = (ImageView) spinner.findViewById(R.id.drop_arrow);
        if (!dropdown) {
            imageView.setImageResource(R.drawable.ic_arrow_drop_down);
            imageView.setVisibility(View.VISIBLE);
            return spinner;
        }
        if (position == 0) {
            imageView.setImageResource(R.drawable.ic_arrow_drop_up);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
        return spinner;
    }

    public String getSpinnerText(int position) {
        return mData.get(position);
    }
}
