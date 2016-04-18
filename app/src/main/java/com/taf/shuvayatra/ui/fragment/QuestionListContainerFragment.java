package com.taf.shuvayatra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yipl.nrna.R;
import com.yipl.nrna.base.BaseFragment;
import com.yipl.nrna.data.utils.Logger;
import com.yipl.nrna.domain.util.MyConstants;
import com.yipl.nrna.ui.adapter.QuestionListContainerAdapter;

import butterknife.Bind;

/**
 * Created by Nirazan-PC on 2/16/2016.
 */
public class QuestionListContainerFragment extends BaseFragment {

    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.tabs)
    TabLayout mTabs;
    @Bind(R.id.data_container)
    LinearLayout mContainer;
    QuestionListContainerAdapter mPagerAdapter;
    MenuItem mMenuFilter;
    Boolean mFiltered;

    public QuestionListContainerFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_question_list_container;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPagerAdapter = new QuestionListContainerAdapter(getChildFragmentManager(), getContext());
        if (savedInstanceState != null) {
            mFiltered = savedInstanceState.getBoolean(MyConstants.Extras.KEY_IS_FILTERED);
        } else {
            mFiltered = false;
        }
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabs.setupWithViewPager(mViewPager);
        Logger.e("quesiton: fragment created");
    }

    @Override
    public void showNewContentInfo() {
        Snackbar.make(mContainer, getString(R.string.message_content_available), Snackbar
                .LENGTH_INDEFINITE)
                .setAction(getString(R.string.action_refresh), new View.OnClickListener() {
                    @Override
                    public void onClick(View pView) {
                        mPagerAdapter.notifyDataSetChanged();
                    }
                })
                .show();
    }

    public void changeFilterIcon(boolean pIsFIltered) {
        if (pIsFIltered) {
            mFiltered = true;
            mMenuFilter.setIcon(R.drawable.ic_filter_applied);
        } else {
            mFiltered = false;
            mMenuFilter.setIcon(R.drawable.ic_filter);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(MyConstants.Extras.KEY_IS_FILTERED, mFiltered);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_filter, menu);
        if (mFiltered)
            mMenuFilter = menu.findItem(R.id.action_filter).setIcon(R.drawable.ic_filter_applied);
        else
            mMenuFilter = menu.findItem(R.id.action_filter).setIcon(R.drawable.ic_filter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getChildFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            FilterDialogFragment newFragment = FilterDialogFragment.newInstance();
            newFragment.setTargetFragment(this, 0);
            newFragment.show(ft, "dialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
