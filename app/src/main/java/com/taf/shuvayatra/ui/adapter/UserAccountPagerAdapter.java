package com.taf.shuvayatra.ui.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.ui.fragment.FavouritePostFragment;
import com.taf.shuvayatra.ui.fragment.UserInfoFragment;

/**
 * Created by umesh on 1/12/17.
 */

public class UserAccountPagerAdapter extends FragmentPagerAdapter {

    private static final int ITEM_COUNT = 2;
    String[] titles;
    private Context context;

    public UserAccountPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FavouritePostFragment.newInstance();
            case 1:
                return UserInfoFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.tab_label_favorites);
            case 1:
                return context.getString(R.string.tab_label_user_info);
            default:
                return "";
        }
    }
}
