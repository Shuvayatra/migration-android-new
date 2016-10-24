package com.taf.shuvayatra.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taf.shuvayatra.R;

public class ZoneAdapter extends BaseAdapter {

    String mZones[];
    LayoutInflater mLayoutInflater;
    Context mContext;

    public ZoneAdapter(Context context, String[] zones){
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mZones = zones;
    }

    @Override
    public int getCount() {
        return mZones.length;
    }

    @Override
    public Object getItem(int position) {
        return mZones[position+1];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      return getCUstomView(parent, position, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCUstomView(parent,position, true);
    }

    public View getCUstomView(ViewGroup parent, int position, boolean dropdown){
        View spinner = mLayoutInflater.inflate(R.layout.view_zone, parent, false);
        TextView textView = (TextView) spinner.findViewById(R.id.filterText);
        if(position == 0) {
            textView.setText(mContext.getString(R.string.info_select_region));
        }else{
            textView.setText(mZones[position-1]);
        }

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
