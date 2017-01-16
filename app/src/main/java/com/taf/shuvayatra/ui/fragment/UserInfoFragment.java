package com.taf.shuvayatra.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taf.data.utils.AppPreferences;
import com.taf.data.utils.Logger;
import com.taf.model.UserInfoModel;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.databinding.ItemUserInfoBinding;
import com.taf.shuvayatra.ui.activity.OnBoardActivity;
import com.taf.util.MyConstants;

import butterknife.OnClick;

/**
 * Created by umesh on 1/12/17.
 */

public class UserInfoFragment extends BaseFragment {

    ItemUserInfoBinding binding;

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.item_user_info;
    }

    @OnClick(R.id.user_info_edit)
    public void onEditPreference() {
        Intent intent = new Intent(getActivity(), OnBoardActivity.class);
        intent.putExtra(MyConstants.Extras.IS_EXIT, true);
        intent.putExtra(UserInfoModel.EXTRA_INFO_MODEL,
                makeUserInfo(getTypedActivity().getPreferences(), getContext()));
        startActivityForResult(intent, 100);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setUser(makeUserInfo(getTypedActivity().getPreferences(), getContext()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            binding.setUser(makeUserInfo(getTypedActivity().getPreferences(), getContext()));
        }
    }

    private static final String TAG = "UserInfoFragment";

    public static UserInfoModel makeUserInfo(AppPreferences appPreferences, Context context) {
        UserInfoModel userInfo = new UserInfoModel();
        userInfo.setName(appPreferences.getUserName());
        if (appPreferences.getBirthday() != Long.MIN_VALUE)
            userInfo.setBirthday(appPreferences.getBirthday());
        String countryInfo = appPreferences.getLocation();
        if (!countryInfo.equalsIgnoreCase(MyConstants.Preferences.DEFAULT_LOCATION) &&
                !countryInfo.equalsIgnoreCase(context.getString(R.string.country_not_decided_yet))) {
            userInfo.setDestinedCountry(TextUtils.split(countryInfo, ",")[1]);
        } else {
            userInfo.setDestinedCountry(null);
        }
        userInfo.setWorkStatus(appPreferences.getPreviousWorkStatus());
        if (appPreferences.getOriginalLocation() != Integer.MIN_VALUE) {
            int id = appPreferences.getOriginalLocation();
            String[] zones = context.getResources().getStringArray(R.array.zones);
            userInfo.setOriginalLocation(zones[id]);
        }
        userInfo.setGender(appPreferences.getGender());
        return userInfo;
    }
}
