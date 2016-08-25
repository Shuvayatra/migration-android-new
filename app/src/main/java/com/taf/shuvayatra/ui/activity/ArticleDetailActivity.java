package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import com.taf.data.utils.Logger;
import com.taf.interactor.UseCaseData;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.FacebookActivity;
import com.taf.shuvayatra.databinding.ArticleDetailDataBinding;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.presenter.PostFavouritePresenter;
import com.taf.shuvayatra.presenter.PostViewCountPresenter;
import com.taf.shuvayatra.ui.interfaces.PostDetailView;
import com.taf.shuvayatra.util.AnalyticsUtil;
import com.taf.shuvayatra.util.SocialShare;
import com.taf.util.MyConstants;

import javax.inject.Inject;

import butterknife.Bind;

public class ArticleDetailActivity extends FacebookActivity implements PostDetailView {

    public static final String KEY_POST = "key_post";



    @Inject
    PostFavouritePresenter mFavouritePresenter;

    @Inject
    PostViewCountPresenter mPostViewCountPresenter;

    Post mPost;
    private boolean mOldFavouriteState;

    @Override
    public int getLayout() {
        return R.layout.activity_article_detail;
    }

    @Override
    public boolean isDataBindingEnabled() {
        return true;
    }

    @Override
    public boolean containsShareOption() {
        return true;
    }

    @Override
    public boolean containsFavouriteOption() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Bind(R.id.btnfb)
    ImageButton btnFacebook;

    @Bind(R.id.btntwit)
    ImageButton btnTwitter;

    @Bind(R.id.btnvib)
    ImageButton btnViber;

    @Bind(R.id.btnwapp)
    ImageButton btnWhatsApp;

    @Bind(R.id.btnmss)
    ImageButton btnMessage;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finishWithResult();
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            share(mPost);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setVisibilityOfSocialIcons();
    }

    @Override
    public void updateFavouriteState() {
        UseCaseData data = new UseCaseData();
        boolean status = !(mPost.isFavourite() != null && mPost.isFavourite());
        data.putBoolean(UseCaseData.FAVOURITE_STATE, status);

        AnalyticsUtil.logFavouriteEvent(getAnalytics(), mPost.getId(), mPost.getTitle(), mPost
                .getType(), status);
        
        mFavouritePresenter.initialize(data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (savedInstanceState != null) {
                mPost = (Post) savedInstanceState.get(KEY_POST);
            } else {
                mPost = (Post) bundle.getSerializable(MyConstants.Extras.KEY_ARTICLE);
                AnalyticsUtil.logViewEvent(getAnalytics(), mPost.getId(), mPost.getTitle(), mPost
                        .getType());
            }
        }
        ((ArticleDetailDataBinding) mBinding).setArticle(mPost);
        mOldFavouriteState = mPost.isFavourite() != null ? mPost.isFavourite() : false;
        initialize();

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setVisibilityOfSocialIcons();
        SocialBtnListener socialBtnListener=new SocialBtnListener(getApplicationContext(),mPost);
        btnFacebook.setOnClickListener(socialBtnListener);
        btnTwitter.setOnClickListener(socialBtnListener);
        btnWhatsApp.setOnClickListener(socialBtnListener);
        btnMessage.setOnClickListener(socialBtnListener);
        btnViber.setOnClickListener(socialBtnListener);

        if (savedInstanceState == null) {
            mPostViewCountPresenter.initialize(null);
        }
    }

    private void setVisibilityOfSocialIcons() {
        SocialShare socialShare=new SocialShare(getApplicationContext());
        if(socialShare.isPackageInstalled("com.twitter.android")){
            btnTwitter.setVisibility(View.VISIBLE);
        }else{
            btnTwitter.setVisibility(View.GONE);
        }
        if(socialShare.isPackageInstalled("com.facebook.katana")){
            btnFacebook.setVisibility(View.VISIBLE);
        }else{
            btnFacebook.setVisibility(View.GONE);
        }
        if(socialShare.isPackageInstalled("com.viber.voip")){
            btnViber.setVisibility(View.VISIBLE);
        }else{
            btnViber.setVisibility(View.GONE);
        }
        if(socialShare.isPackageInstalled("com.whatsapp")){
            btnWhatsApp.setVisibility(View.VISIBLE);
        }else{
            btnWhatsApp.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finishWithResult();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finishWithResult() {
        Intent data = new Intent();
        Logger.e("ArticleDetailActivity", "view count: " + mPost.getUnSyncedViewCount());
        data.putExtra(MyConstants.Extras.KEY_FAVOURITE_STATUS, mPost.isFavourite());
        data.putExtra(MyConstants.Extras.KEY_FAVOURITE_COUNT, mPost.getLikes());
        data.putExtra(MyConstants.Extras.KEY_VIEW_COUNT, mPost.getUnSyncedViewCount());
        data.putExtra(MyConstants.Extras.KEY_SHARE_COUNT, mPost.getUnSyncedShareCount());
        setResult(RESULT_OK, data);
        finish();
    }

    private void initialize() {
        DaggerDataComponent.builder()
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .dataModule(new DataModule(mPost.getId()))
                .build()
                .inject(this);
        mPostViewCountPresenter.attachView(this);
        mFavouritePresenter.attachView(this);
        mPostShareCountPresenter.attachView(this);
    }

    @Override
    public void onPostFavouriteStateUpdated(Boolean status) {
        boolean newFavouriteState = status ? !mOldFavouriteState : mOldFavouriteState;
        mPost.setIsFavourite(newFavouriteState);
        int likes = mPost.getLikes() == null ? 0 : mPost.getLikes();
        mPost.setLikes(newFavouriteState == mOldFavouriteState
                ? likes
                : newFavouriteState ? likes + 1 : likes - 1);
        ((ArticleDetailDataBinding) mBinding).setArticle(mPost);
        mOldFavouriteState = mPost.isFavourite();
        invalidateOptionsMenu();
    }

    @Override
    public void onViewCountUpdated() {
        mPost.setUnSyncedViewCount(mPost.getUnSyncedViewCount() + 1);
        Logger.e("ArticleDetailActivity", "on view updated:  view count" + mPost.getUnSyncedViewCount());
    }

    @Override
    public void onShareCountUpdate() {
        mPost.setUnSyncedShareCount(mPost.getUnSyncedShareCount() + 1);
        ((ArticleDetailDataBinding) mBinding).setArticle(mPost);
    }

    @Override
    public void showErrorView(String pErrorMessage) {
        Snackbar.make(mBinding.getRoot(), pErrorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mPost != null) {
            menu.findItem(R.id.action_favourite).setIcon((mPost.isFavourite() != null && mPost
                    .isFavourite()) ? R.drawable.icon_favourite : R.drawable.icon_not_favourite);
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_POST, mPost);
        super.onSaveInstanceState(outState);
    }

    private class SocialBtnListener implements View.OnClickListener{
        private Context context;
        private Post mPost;

        public SocialBtnListener(Context context,Post mPost){
                this.context=context;
                this.mPost=mPost;
        }

        @Override
        public void onClick(View v) {
            SocialShare socialShare=new SocialShare(context);
            switch(v.getId()){
                case R.id.btnfb:
                    try {
                        startActivity(socialShare.getFacebookIntent(mPost.getShareUrl()));
                    }catch(Exception e){
                        startActivity(socialShare.getPlayStoreIntent("com.facebook.katana"));
                    }
                    break;
                case R.id.btnvib:
                    try {
                        startActivity(socialShare.getViberIntent(mPost.getShareUrl()));
                    }catch(Exception e){
                        startActivity(socialShare.getPlayStoreIntent("com.viber.voip"));
                    }
                    break;
                case R.id.btnwapp:
                    try {
                        startActivity(socialShare.getWhatsApp(mPost.getShareUrl()));
                    }catch(Exception e){
                        startActivity(socialShare.getPlayStoreIntent("com.whatsapp"));
                    }
                    break;
                case R.id.btntwit:
                    try {
                        startActivity(socialShare.getTwitterIntent(mPost.getShareUrl()));
                    }catch(Exception e){
                        startActivity(socialShare.getPlayStoreIntent("com.twitter.android"));
                    }
                    break;
                case R.id.btnmss:
                    try {
                        startActivity(socialShare.getSmsIntent(mPost.getShareUrl()));
                    }catch(Exception e){

                    }
                    break;
            }
        }
    }
}
