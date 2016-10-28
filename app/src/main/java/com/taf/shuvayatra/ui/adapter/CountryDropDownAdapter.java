package com.taf.shuvayatra.ui.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by rakeeb on 10/27/16.
 */
public class CountryDropDownAdapter extends DropDownAdapter {

    public CountryDropDownAdapter(Context context, List<String> dataList) {
        super(context, dataList);
    }

    @Override
    public String getSpinnerText(int position) {
        if (position == 0)
            return super.getSpinnerText(position);
        else
            return getData().get(position).split(",")[1];
    }
}
