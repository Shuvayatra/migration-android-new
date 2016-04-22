package com.taf.shuvayatra.ui.custom;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.taf.data.utils.Logger;

/**
 * Created by Nirazan-PC on 4/21/2016.
 */
public class MarginItemDecoration extends RecyclerView.ItemDecoration {

    private int margin;


    //margin to only one direction // either left or right or bottom or top
    public MarginItemDecoration(Context pContext, @DimenRes int margin){
        this.margin = pContext.getResources().getDimensionPixelOffset(margin);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int size = parent.getChildCount();
        for(int i=0;i<size;i++){
           if(i%2==0){
               outRect.right = margin;
               outRect.left = 0;
           } else{
               outRect.left = margin;
               outRect.right =  0;
           }
            outRect.bottom = margin;
        }

    }

}
