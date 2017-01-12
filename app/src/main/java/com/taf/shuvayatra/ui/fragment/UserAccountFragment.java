package com.taf.shuvayatra.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import com.taf.model.BaseModel;
import com.taf.model.Post;
import com.taf.model.UserInfoModel;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.UserAccountPresenter;
import com.taf.shuvayatra.ui.activity.ArticleDetailActivity;
import com.taf.shuvayatra.ui.activity.AudioDetailActivity;
import com.taf.shuvayatra.ui.activity.VideoDetailActivity;
import com.taf.shuvayatra.ui.adapter.ListAdapter;
import com.taf.shuvayatra.ui.custom.EmptyStateRecyclerView;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.shuvayatra.ui.views.UserAccountView;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by umesh on 1/11/17.
 */

public class UserAccountFragment extends BaseFragment implements UserAccountView, ListItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "UserAccountFragment";
    private static final int REQUEST_CODE_POST_DETAIL = 1001;

    @Inject
    UserAccountPresenter userAccountPresenter;

    @BindView(R.id.recycler_view)
    EmptyStateRecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    RelativeLayout mEmptyView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    ListAdapter<BaseModel> mAdapter;
    private int listItemSelection;
    UserInfoModel mUserInfo;

    public static UserAccountFragment newInstance() {

        UserAccountFragment fragment = new UserAccountFragment();
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_user_account;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpAdapter();
        initialize();
        mUserInfo = getUserInfo();
        mAdapter.getDataCollection().add(mUserInfo);
        userAccountPresenter.initialize(null);
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .activityModule(getTypedActivity().getActivityModule())
                .applicationComponent(getTypedActivity().getApplicationComponent())
                .dataModule(new DataModule())
                .build()
                .inject(this);

        userAccountPresenter.attachView(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setUpAdapter() {
        mAdapter = new ListAdapter<BaseModel>(getContext(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mSwipeRefreshLayout, pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void renderPosts(List<Post> posts) {
        List<BaseModel> models = new ArrayList<>();
        models.add(mUserInfo);
        models.addAll(posts);
        mAdapter.setDataCollection(models);
    }

    @Override
    public void showLoadingView() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingView() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onListItemSelected(BaseModel pModel, int pIndex) {
        Intent intent = null;
        listItemSelection = pIndex;
        switch (pModel.getDataType()) {
            case MyConstants.Adapter.TYPE_VIDEO:
                intent = new Intent(getContext(), VideoDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_ID, pModel.getId());
                break;
            case MyConstants.Adapter.TYPE_TEXT:
                intent = new Intent(getContext(), ArticleDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_ID, pModel.getId());
                break;
            case MyConstants.Adapter.TYPE_AUDIO:
                intent = new Intent(getContext(), AudioDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_ID, pModel.getId());
                break;
        }
        if (intent != null)
            startActivityForResult(intent, REQUEST_CODE_POST_DETAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BaseActivity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_POST_DETAIL) {
                int shareCount = data.getIntExtra(MyConstants.Extras.KEY_SHARE_COUNT, 0);
                ((Post) mAdapter.getDataCollection().get(listItemSelection)).setShare(shareCount);
                int favCount = data.getIntExtra(MyConstants.Extras.KEY_FAVOURITE_COUNT, 0);
                ((Post) mAdapter.getDataCollection().get(listItemSelection)).setLikes(favCount);
                if (!data.getBooleanExtra(MyConstants.Extras.KEY_FAVOURITE_STATUS, true))
                    mAdapter.getDataCollection().remove(listItemSelection);
                mAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onRefresh() {
        userAccountPresenter.initialize(null);
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
        int worStatusId = getTypedActivity().getPreferences().getPreviousWorkStatus();
        userInfo.setWorkStatus(getString(worStatusId));
        int id = getTypedActivity().getPreferences().getOriginalLocation();
        String[] zones = getResources().getStringArray(R.array.zones);
        userInfo.setOrignalLocation(zones[id]);
        userInfo.setGender(getTypedActivity().getPreferences().getGender());
        userInfo.setDataType(MyConstants.Adapter.TYPE_USER_INFO);
        return userInfo;
    }

}
