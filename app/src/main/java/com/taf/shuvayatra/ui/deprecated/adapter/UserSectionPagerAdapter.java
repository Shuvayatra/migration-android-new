package com.taf.shuvayatra.ui.deprecated.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.taf.shuvayatra.R;
import com.taf.shuvayatra.ui.deprecated.fragment.FeedFragment;
import com.taf.shuvayatra.ui.deprecated.fragment.NotificationsFragment;

public class UserSectionPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String mTitles[] = new String[PAGE_COUNT];

    public UserSectionPagerAdapter(FragmentManager pFragmentManager, Context context) {
        super(pFragmentManager);
        mTitles = context.getResources().getStringArray(R.array.user_section_tabs);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NotificationsFragment();
            case 1:
                return FeedFragment.newInstance(true);
            default:
                throw new IllegalStateException();
        }
    }
}
