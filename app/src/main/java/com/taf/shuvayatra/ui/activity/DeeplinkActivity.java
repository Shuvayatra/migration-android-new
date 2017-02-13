package com.taf.shuvayatra.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.taf.data.utils.Logger;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.base.PostDetailActivity;
import com.taf.util.MyConstants;

import butterknife.BindView;

@Deprecated
public class DeeplinkActivity extends BaseActivity {

    private static final String TAG = "DeeplinkActivity";

    @BindView(R.id.fragment_container)
    FrameLayout mContainer;

    @Override
    public int getLayout() {
        return R.layout.activity_deep_link;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri data = getIntent().getData();
        Logger.e(TAG, ">>> deep link: " + data.getPath());
        handleDeepLink(data);

        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public String screenName() {
        return "IGNORED";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleDeepLink(Uri data) {
        Fragment fragment = null;

        if (data.getHost().equals("post")) {
            String postType = data.getQueryParameter("post_type");
            String postId = data.getQueryParameter("post_id");

            if (postType == null || postId == null) {
                Snackbar.make(mContainer, "Could not load the post detail. Sufficient data not " +
                        "provided.", Snackbar.LENGTH_LONG).show();
                return;
            }

            Intent intent = null;
            if (postType.equalsIgnoreCase(Post.TYPE_AUDIO)) {
                intent = new Intent(this, AudioDetailActivity.class);
            } else if (postType.equalsIgnoreCase(Post.TYPE_VIDEO)) {
                intent = new Intent(this, VideoDetailActivity.class);
            } else if (postType.equalsIgnoreCase(Post.TYPE_TEXT)) {
                intent = new Intent(this, ArticleDetailActivity.class);
            } else if(postType.equalsIgnoreCase(Post.TYPE_PLACE)) {
                intent = new Intent(this, PlaceDetailActivity.class);
            }

            if (intent != null) {
                intent.putExtra(MyConstants.Extras.KEY_ID, Long.valueOf(postId));
                startActivity(intent);
            }
            finish();
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, "deep-link")
                    .commit();
        }
    }
}
