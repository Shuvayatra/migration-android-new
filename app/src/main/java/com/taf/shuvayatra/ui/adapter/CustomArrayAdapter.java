package com.taf.shuvayatra.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taf.model.Category;
import com.taf.shuvayatra.R;

import java.util.List;

/**
 * Created by Nirazan-PC on 4/22/2016.
 */
public class CustomArrayAdapter extends BaseAdapter {

    List<Category> mCategories;
    LayoutInflater mInflater;

    public CustomArrayAdapter(Context context, List<Category> pCategories) {
        mInflater = LayoutInflater.from(context);
        mCategories = pCategories;
    }

    @Override
    public int getCount() {
        return mCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return mCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
//        return mCategories.get(position).getId();
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(parent,position, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(parent, position, true);
    }

    public View getCustomView(ViewGroup parent, int position, boolean dropdown){
        View spinner = mInflater.inflate(R.layout.view_filter_spinner, parent, false);
        TextView textView = (TextView) spinner.findViewById(R.id.filterText);
        textView.setText(mCategories.get(position).getTitle());
        ImageView imageView = (ImageView) spinner.findViewById(R.id.drop_arrow);
        if(!dropdown) {
            imageView.setImageResource(R.drawable.ic_arrow_drop_down);
            imageView.setVisibility(View.VISIBLE);
            return spinner;
        }
        if(position == 0){
            imageView.setImageResource(R.drawable.ic_arrow_drop_up);
            imageView.setVisibility(View.VISIBLE);
        }else{
            imageView.setVisibility(View.GONE);
        }
        return spinner;
    }
}
