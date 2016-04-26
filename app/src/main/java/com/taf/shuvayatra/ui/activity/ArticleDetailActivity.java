package com.taf.shuvayatra.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.databinding.ArticleDetailDataBinding;
import com.taf.util.MyConstants;

public class ArticleDetailActivity extends BaseActivity {

    Post mPost;

    @Override
    public int getLayout() {
        return R.layout.activity_article_detail;
    }

    @Override
    public boolean isDataBindingEnabled() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPost = (Post) bundle.getSerializable(MyConstants.Extras.KEY_ARTICLE);
        }
        ((ArticleDetailDataBinding) mBinding).setArticle(mPost);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
