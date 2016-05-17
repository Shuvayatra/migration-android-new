package com.taf.shuvayatra.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;
import com.taf.data.utils.Logger;
import com.taf.model.BaseModel;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.presenter.PostShareCountPresenter;
import com.taf.util.MyConstants;

import javax.inject.Inject;

import bolts.AppLinks;

/**
 * Created by Nirazan-PC on 4/28/2016.
 */
public abstract class FacebookActivity extends BaseActivity {

    @Inject
    protected PostShareCountPresenter mPostShareCountPresenter;

    private CallbackManager mCallback;
    private FacebookCallback<Sharer.Result> mFbCallBack;
    private ShareDialog mShareDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        initializeFbShare();
    }

    private void initializeFbShare() {
        mShareDialog = new ShareDialog(this);
        mCallback = CallbackManager.Factory.create();
        mFbCallBack = new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Logger.e("ArticleDetailActivity", "succcess");
                mPostShareCountPresenter.initialize(null);
            }

            @Override
            public void onCancel() {
                Logger.e("ArticleDetailActivity", "canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Logger.e("ArticleDetailActivity", "error: " + error.toString());
                error.printStackTrace();
            }
        };

        LoginManager.getInstance().registerCallback(mCallback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult result) {
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        });

        mShareDialog.registerCallback(mCallback, mFbCallBack);
    }

    public boolean share(final BaseModel pModel){
        try {
            Intent fbIntent = new Intent(Intent.ACTION_SEND);
            fbIntent.setType("text/plain");
//            fbIntent.putExtra(Intent.EXTRA_TEXT, (Html.fromHtml(((Post) pModel).getTitle()).toString()));
//            fbIntent.putExtra(Intent.EXTRA_TEXT, ((Post) pModel).getDescription());
//                        fbIntent.putExtra(Intent.EXTRA_TEXT, ((Post) pModel).getTitle());
            //// TODO: 5/16/2016 play store
            fbIntent.putExtra(Intent.EXTRA_TEXT,"https://www.play.google.com/store/apps/details?id=com.taf.shuvayatra");

            startActivity(fbIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    //// TODO: 5/16/2016 use after app verification
    public boolean share1(final BaseModel pModel) {
        if (mShareDialog.canShow(ShareOpenGraphContent.class)) {
            mShareDialog.show(getShareContent(pModel));
            return true;
        }
        return false;
//        Bundle params = new Bundle();
//        params.putString("og:title", ((Post) pModel).getTitle());
//        params.putString("og:description", ((Post) pModel).getDescription());
//        params.putString("al:android:app_name",getString(R.string.app_name));
//        params.putString("al:android:package","com.taf.shuvayatra");


//        GraphRequest request = new GraphRequest(
//                AccessToken.getCurrentAccessToken(),
//                "me/objects/nrnaapp:post",
//                params,
//                HttpMethod.POST
//        );
//        request.setCallback(new GraphRequest.Callback() {
//            @Override
//            public void onCompleted(GraphResponse response) {
//                Logger.e("ArticleDetailActivity", "response:  "+response.toString());
//            }
//        });
//         GraphRequestAsyncTask task = request.executeAsync();
//        task.execute();
//        Logger.e("ArticleDetailActivity", "response anothr: "+ response.toString());
// handle the response
//    }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallback.onActivityResult(requestCode, resultCode, data);
    }

    public ShareOpenGraphContent getShareContent(BaseModel pModel) {
        String title, description;
        switch (pModel.getDataType()) {
            default:
            case MyConstants.Adapter.TYPE_AUDIO:
            case MyConstants.Adapter.TYPE_VIDEO:
            case MyConstants.Adapter.TYPE_TEXT:
                title = ((Post) pModel).getTitle();
                description = Html.fromHtml(((Post) pModel).getDescription()).toString();
                break;
        }

        ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                .putString("og:type", "nrnaapp:post")
                .putString("og:title", title)
                .putString("og:description", description)
                .putString("og:url","https://fb.me/988277937930762?id="+pModel.getId())
                .putString("al:android:url","shuvayatra://taf.posts")
                .putString("al:android:app_name",getString(R.string.app_name))
                .putString("al:android:package","com.taf.shuvayatra")
                .build();
        ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                .setActionType("nrnaapp:share")
                .putObject("post", object)
                .build();
        ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                .setPreviewPropertyName("post")
                .setAction(action)
                .build();
        return content;
    }

}
