package com.taf.shuvayatra.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taf.model.Category;
import com.taf.shuvayatra.R;

import java.util.List;

/**
 * Created by Nirazan-PC on 4/22/2016.
 */
public class CustomArrayAdapter extends ArrayAdapter {

    List<Category> mCategories;
    LayoutInflater mInflater;



    public CustomArrayAdapter(Context context, int resource, List<Category> pCategories) {
        super(context, resource, pCategories);
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
        return mCategories.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(parent,position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(parent, position);
    }

    public View getCustomView(ViewGroup parent, int position){
        View spinner = mInflater.inflate(R.layout.view_filter_spinner, parent, false);
        TextView textView = (TextView) spinner.findViewById(R.id.filterText);
        textView.setText(mCategories.get(position).getName());
        if(position == 0){
//            ImageView imageView = spinner.findViewById(R.id);
        }
        return spinner;
    }
}
