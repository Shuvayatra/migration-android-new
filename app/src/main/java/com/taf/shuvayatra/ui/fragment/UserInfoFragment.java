package com.taf.shuvayatra.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taf.model.UserInfoModel;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.databinding.ItemUserInfoBinding;
import com.taf.util.MyConstants;

/**
 * Created by umesh on 1/12/17.
 */

public class UserInfoFragment extends BaseFragment {

    UserInfoModel mUserInfo;
    ItemUserInfoBinding binding;
    public static UserInfoFragment newInstance(){
        return new UserInfoFragment();
    }
    @Override
    public int getLayout() {
        return R.layout.item_user_info;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUserInfo = getUserInfo();
        binding.setUser(mUserInfo);

    }

    public UserInfoModel getUserInfo() {
        UserInfoModel userInfo = new UserInfoModel();
        userInfo.setName(getTypedActivity().getPreferences().getUserName());
        userInfo.setBirthday(getTypedActivity().getPreferences().getBirthday());
        String countryInfo = getTypedActivity().getPreferences().getLocation();
        if (!countryInfo.equalsIgnoreCase(MyConstants.Preferences.DEFAULT_LOCATION) && !countryInfo.equalsIgnoreCase(getString(R.string.country_not_decided_yet))) {

            userInfo.setDestinedCountry(TextUtils.split(countryInfo, ",")[1]);
        } else {
            userInfo.setDestinedCountry(null);
        }
        userInfo.setWorkStatus(getTypedActivity().getPreferences().getPreviousWorkStatus());
        int id = getTypedActivity().getPreferences().getOriginalLocation();
        String[] zones = getResources().getStringArray(R.array.zones);
        userInfo.setOrignalLocation(zones[id]);
        userInfo.setGender(getTypedActivity().getPreferences().getGender());
        return userInfo;
    }
}
