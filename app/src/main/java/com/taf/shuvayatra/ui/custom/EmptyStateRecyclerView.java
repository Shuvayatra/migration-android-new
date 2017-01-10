package com.taf.shuvayatra.ui.custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.taf.shuvayatra.R;

public class EmptyStateRecyclerView extends RecyclerView {

    View emptyView;
    AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            checkIfEmpty();
        }
    };

    public EmptyStateRecyclerView(Context context) {
        super(context);
    }

    public EmptyStateRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyStateRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }

    public void setEmptyMessage(String pMessage) {
        if (emptyView != null) {
            TextView textView = (TextView) emptyView.findViewById(R.id.empty_message);
            textView.setText(pMessage);
        }
    }

    void checkIfEmpty() {
        if (emptyView != null) {
            Log.e("", "checkIfEmpty: " + getAdapter().getItemCount());

            emptyView.setVisibility(getAdapter().getItemCount() > 0 ? GONE : VISIBLE);
        }
    }
}