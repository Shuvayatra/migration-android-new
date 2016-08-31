package com.taf.shuvayatra.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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

    @Bind(R.id.row1)
    LinearLayout laySocialRow;

    @Bind(R.id.rowTop)
    LinearLayout rowTop;

    @Bind(R.id.btnShare)
    ImageButton btnShare;

    @Bind(R.id.content_webView)
    WebView contentWebview;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finishWithResult();
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            startActivity(Intent.createChooser(new SocialShare(getApplicationContext()).getGenericShare(mPost), "Share using"));
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

        myWebViewClient myWebViewClient = new myWebViewClient();
        contentWebview.setWebViewClient(myWebViewClient);




        mOldFavouriteState = mPost.isFavourite() != null ? mPost.isFavourite() : false;
        initialize();

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int noOfSocialIcons=setVisibilityOfSocialIcons();
        iconAdjustment(noOfSocialIcons);
        SocialBtnListener socialBtnListener=new SocialBtnListener(getApplicationContext(),mPost);
        btnFacebook.setOnClickListener(socialBtnListener);
        btnTwitter.setOnClickListener(socialBtnListener);
        btnWhatsApp.setOnClickListener(socialBtnListener);
        btnMessage.setOnClickListener(socialBtnListener);
        btnViber.setOnClickListener(socialBtnListener);
        btnShare.setOnClickListener(socialBtnListener);

        if (savedInstanceState == null) {
            mPostViewCountPresenter.initialize(null);
        }
    }

    private void iconAdjustment(int noOfSocialIcons){
        switch (noOfSocialIcons)
        {
            case 3:
                laySocialRow.setWeightSum(3);
                break;
            case 4:
                laySocialRow.setWeightSum(4);
                break;
            case 5:
                laySocialRow.setWeightSum(5);
                break;
            case 6:
                laySocialRow.setWeightSum(6);
                break;
            default:
                laySocialRow.setWeightSum(2);
                break;

        }

    }

    private int setVisibilityOfSocialIcons() {
        SocialShare socialShare=new SocialShare(getApplicationContext());
        int count=2 ;
        if(socialShare.isPackageInstalled("com.twitter.android")){
            btnTwitter.setVisibility(View.VISIBLE);
            count++;
        }else{
            btnTwitter.setVisibility(View.GONE);
        }
        if(socialShare.isPackageInstalled("com.facebook.katana")){
            btnFacebook.setVisibility(View.VISIBLE);
            count++;
        }else{
            btnFacebook.setVisibility(View.GONE);
        }
        if(socialShare.isPackageInstalled("com.viber.voip")){
            btnViber.setVisibility(View.VISIBLE);
            count++;
        }else{
            btnViber.setVisibility(View.GONE);
        }
        if(socialShare.isPackageInstalled("com.whatsapp")){
            btnWhatsApp.setVisibility(View.VISIBLE);
            count++;
        }else{
            btnWhatsApp.setVisibility(View.GONE);
        }
        return count;
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
    private class myWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            rowTop.setVisibility(View.VISIBLE);
            contentWebview.getSettings().setJavaScriptEnabled(true);
            contentWebview.loadUrl("javascript:document.body.style.setProperty(\"color\", \"#373D3F\");");

        }

    }
    private class SocialBtnListener  implements View.OnClickListener {
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
                        startActivity(socialShare.getFacebookIntent(mPost));
                    }catch(Exception e){
                        startActivity(socialShare.getPlayStoreIntent("com.facebook.katana"));
                    }
                    break;
                case R.id.btnvib:
                    try {
                        startActivity(socialShare.getViberIntent(mPost));
                    }catch(Exception e){
                        startActivity(socialShare.getPlayStoreIntent("com.viber.voip"));
                    }
                    break;
                case R.id.btnwapp:
                    try {
                        startActivity(socialShare.getWhatsApp(mPost));
                    }catch(Exception e){
                        startActivity(socialShare.getPlayStoreIntent("com.whatsapp"));
                    }
                    break;
                case R.id.btntwit:
                    try {
                        startActivity(socialShare.getTwitterIntent(mPost));
                    }catch(Exception e){
                        startActivity(socialShare.getPlayStoreIntent("com.twitter.android"));
                    }
                    break;
                case R.id.btnmss:
                    try {
                        startActivity(socialShare.getSmsIntent(mPost));
                    }catch(Exception e){

                    }
                    break;
                case R.id.btnShare:
                    try {
                        startActivity(Intent.createChooser(socialShare.getGenericShare(mPost), "Share using"));
                    }catch(Exception e){

                    }
                    break;
            }
        }
    }




}
