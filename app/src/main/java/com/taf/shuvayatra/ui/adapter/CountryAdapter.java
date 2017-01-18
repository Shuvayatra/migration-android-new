package com.taf.shuvayatra.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taf.data.utils.Logger;
import com.taf.model.BaseModel;
import com.taf.model.Country;
import com.taf.model.HeaderItem;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.databinding.ItemCountryHeaderDataBinding;
import com.taf.shuvayatra.databinding.ItemCountryListDataBinding;
import com.taf.shuvayatra.databinding.ItemCountryListSelectedDataBinding;
import com.taf.shuvayatra.ui.activity.DestinationDetailActivity;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    public static final String TAG = "CountryAdapter";
    List<BaseModel> mCountries;
    LayoutInflater mLayoutInflater;
    Context mContext;

    public CountryAdapter(Context context) {
        mCountries = new ArrayList<>();
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setCountries(List<BaseModel> countries) {
        mCountries = countries;
        notifyDataSetChanged();
    }

    public List<BaseModel> getCountries() {
        return mCountries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding;
        switch (viewType) {
            case MyConstants.Adapter.TYPE_COUNTRY:
                binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_country_list, parent, false);
                return new ViewHolder<>(binding);
            case MyConstants.Adapter.TYPE_COUNTRY_SELECTED:
                binding = DataBindingUtil.inflate(mLayoutInflater,
                        R.layout.item_country_list_selected, parent, false);
                return new ViewHolder(binding);
            case MyConstants.Adapter.TYPE_COUNTRY_HEADER:
                binding = DataBindingUtil.inflate(mLayoutInflater,
                        R.layout.item_country_header, parent, false);
                return new ViewHolder(binding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case MyConstants.Adapter.TYPE_COUNTRY:
                Logger.e(TAG, position + "((Country) mCountries.get(position)).getTitleEnglish();: " + ((Country) mCountries.get(position)).getTitleEnglish());

                ((ItemCountryListDataBinding) holder.mBinding)
                        .setCountry((Country) mCountries.get(position));
                return;
            case MyConstants.Adapter.TYPE_COUNTRY_HEADER:
                ((ItemCountryHeaderDataBinding) holder.mBinding)
                        .setTitle(((HeaderItem) mCountries.get(position)).getTitle());
                return;
            case MyConstants.Adapter.TYPE_COUNTRY_SELECTED:
                ((ItemCountryListSelectedDataBinding) holder.mBinding)
                        .setCountry((Country) mCountries.get(position));
                Logger.e(TAG, "((Country) mCountries.get(position)).getTitleEnglish();: " + ((Country) mCountries.get(position)).getTitleEnglish());

        }
    }

    @Override
    public int getItemViewType(int position) {
        return mCountries.get(position).getDataType();
    }

    @Override
    public int getItemCount() {
        return mCountries.size();
    }

    public class ViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

        public T mBinding;

        public ViewHolder(T binding) {
            super(binding.getRoot());
            mBinding = binding;
            if (mBinding instanceof ItemCountryListSelectedDataBinding || mBinding instanceof ItemCountryListDataBinding) {
                mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Logger.e(TAG, "country clicked: ");
                        Country country = null;
                        if (mBinding instanceof ItemCountryListSelectedDataBinding)
                            country = ((ItemCountryListSelectedDataBinding) mBinding).getCountry();
                        else if (mBinding instanceof ItemCountryListDataBinding)
                            country = ((ItemCountryListDataBinding) mBinding).getCountry();
                        Intent intent = new Intent(mContext, DestinationDetailActivity.class);
                        intent.putExtra(MyConstants.Extras.KEY_COUNTRY, country);
                        mContext.startActivity(intent);
                    }
                });
            }
        }
    }
}
