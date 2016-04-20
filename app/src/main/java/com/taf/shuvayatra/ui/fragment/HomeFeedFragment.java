package com.taf.shuvayatra.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.taf.data.utils.Logger;
import com.taf.model.BaseModel;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.adapter.ListAdapter;
import com.taf.shuvayatra.base.BaseFragment;
import com.taf.shuvayatra.di.component.DaggerDataComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.ui.activity.ArticleDetailActivity;
import com.taf.shuvayatra.ui.activity.MainActivity;
import com.taf.shuvayatra.ui.activity.VideoDetailActivity;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Nirazan-PC on 4/19/2016.
 */
public class HomeFeedFragment extends BaseFragment implements ListItemClickListener {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Override
    public int getLayout() {
        return R.layout.fragment_home_feed;
    }

    public static HomeFeedFragment newInstance(){
        HomeFeedFragment homeFeedFragment = new HomeFeedFragment();
        return homeFeedFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListAdapter<Post> adapter = new ListAdapter<Post>(getContext(),dummyPost(),this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);

    }

    public List<Post> dummyPost(){
        Logger.e("HomeFeedFragment", "dummy created");
        String[] types={"audio", "video", "text","news"};
        List<Post> posts = new ArrayList<>();
        for(int i=0; i<10;i++){
            Post post = new Post();
            post.setDescription("This is description of post "+i);
            post.setLikes(10);
            post.setShare(100);
            post.setTitle("Title of "+types[i%4]+i);
            post.setCategory("Category A");
//            post.setDataType(i%4);
            post.setType(types[i%4]);
            posts.add(post);
        }
        return posts;
    }

    @Override
    public void onListItemSelected(BaseModel pModel) {
        Intent intent = null;
        switch (pModel.getDataType()) {
            case MyConstants.Adapter.TYPE_VIDEO:
                intent = new Intent(getContext(), VideoDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_VIDEO,pModel);
                break;
            case MyConstants.Adapter.TYPE_TEXT:
                intent = new Intent(getContext(), ArticleDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_ARTICLE,pModel);
                break;
            case MyConstants.Adapter.TYPE_NEWS:
                intent = new Intent(getContext(), ArticleDetailActivity.class);
                intent.putExtra(MyConstants.Extras.KEY_ARTICLE, pModel);
                break;
        }
        if(intent!=null)
            startActivity(intent);
    }

    @Override
    public void onListItemSelected(List<BaseModel> pCollection, int pIndex) {

    }
}
