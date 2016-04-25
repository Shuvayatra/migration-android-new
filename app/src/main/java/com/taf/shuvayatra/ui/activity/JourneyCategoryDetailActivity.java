package com.taf.shuvayatra.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.taf.data.utils.Logger;
import com.taf.model.Category;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.databinding.ActivityJourneyCategoryDetailBinding;
import com.taf.shuvayatra.databinding.JourneyCategoryDataBinding;
import com.taf.shuvayatra.ui.fragment.FeedFragment;
import com.taf.util.MyConstants;

import java.util.List;

public class JourneyCategoryDetailActivity extends BaseActivity {

    Category mCategory;
    List<Category> mSubCategories;

    @Override
    public int getLayout() {
        return R.layout.activity_journey_category_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            mCategory = (Category) bundle.get(MyConstants.Extras.KEY_CATEGORY);
            mSubCategories = (List<Category>) bundle.getSerializable(MyConstants.Extras.KEY_SUBCATEGORY);
            Logger.e("JourneyCategoryDetailActivity", "SUb Categoreis"+mSubCategories);
        }
        ((ActivityJourneyCategoryDetailBinding) mBinding).setCategory(mCategory);
        addFeedFragment();
    }
    public void addFeedFragment(){
        FeedFragment fragment = FeedFragment.newInstance(true,mCategory.getCategoryId());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isDataBindingEnabled() {
        return true;
    }

    public List<Category> getSubCategories(){
        return mSubCategories;
    }
}
