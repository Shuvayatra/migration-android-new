package com.taf.shuvayatra.base;

import android.os.Bundle;
import android.view.MenuItem;

import com.taf.data.utils.Logger;
import com.taf.model.Category;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.ui.fragment.FeedFragment;
import com.taf.util.MyConstants;

import java.util.List;

/**
 * Created by Nirazan-PC on 4/25/2016.
 */
public abstract class CategoryDetailActivity extends BaseActivity{

    public Category mCategory;
    public List<Category> mSubCategories;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            mCategory = (Category) bundle.get(MyConstants.Extras.KEY_CATEGORY);
            mSubCategories = (List<Category>) bundle.getSerializable(MyConstants.Extras.KEY_SUBCATEGORY);
            Logger.e("CategoryDetailActivity", "SUb Categoreis"+mSubCategories);
        }

        addFeedFragment();
    }

    public void addFeedFragment(){
        FeedFragment fragment = FeedFragment.newInstance(true,mCategory.getCategoryId());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
    }

    @Override
    public boolean isDataBindingEnabled() {
        return true;
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

    public List<Category> getSubCategories(){
        return mSubCategories;
    }

}
