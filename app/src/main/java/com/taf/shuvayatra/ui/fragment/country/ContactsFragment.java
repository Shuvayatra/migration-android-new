package com.taf.shuvayatra.ui.fragment.country;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yipl.nrna.R;
import com.yipl.nrna.base.BaseFragment;
import com.yipl.nrna.databinding.CountryContactsDataBinding;
import com.yipl.nrna.domain.model.Country;
import com.yipl.nrna.domain.util.MyConstants;

public class ContactsFragment extends BaseFragment {

    CountryContactsDataBinding mBinding;
    Country mCountry;

    public ContactsFragment() {
        // Required empty public constructor
    }

    public static ContactsFragment newInstance(Country pCountry) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle data = new Bundle();
        data.putSerializable(MyConstants.Extras.KEY_Country, pCountry);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_country_contacts;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        //super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        mBinding.setCountry(mCountry);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCountry = (Country) getArguments().getSerializable(MyConstants.Extras.KEY_Country);
    }
}
