package com.taf.shuvayatra.ui.adapter;

import android.content.Context;

import com.taf.data.utils.Logger;
import com.taf.model.Country;
import com.taf.shuvayatra.R;

import java.util.List;

/**
 * Created by rakeeb on 10/27/16.
 */
public class CountryDropDownAdapter extends DropDownAdapter {

    private static final String TAG = "CountryDropDownAdapter";

    public CountryDropDownAdapter(Context context, List<String> dataList) {
        super(context, dataList);
    }

    @Override
    public String getSpinnerText(int position) {
        try {
            if (position == 0)
                return super.getSpinnerText(position);
            else {
                // getting exception here cause split is not available
                if (getData().get(position).equalsIgnoreCase(mContext.getString(R.string.country_not_decided_yet)))
                    return getData().get(position);
                return getData().get(position).split(",")[Country.INDEX_TITLE];
            }
        } catch (IndexOutOfBoundsException e) {
            Logger.e(TAG, String.format("index out of bounds exception for position: %d, with data: %s", position,
                    getData()));
            return null;
        }
    }
}
