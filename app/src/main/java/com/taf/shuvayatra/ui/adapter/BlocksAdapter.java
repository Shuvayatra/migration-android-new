package com.taf.shuvayatra.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.taf.data.utils.Logger;
import com.taf.model.Block;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.databinding.BlockCountryWidgetDataBinding;
import com.taf.shuvayatra.databinding.BlockListDataBinding;
import com.taf.shuvayatra.databinding.BlockNoticeDataBinding;
import com.taf.shuvayatra.databinding.BlockSliderDataBinding;
import com.taf.shuvayatra.ui.fragment.CountryWidgetFragment;

import java.util.List;

/**
 * Created by julian on 10/18/16.
 */

public class BlocksAdapter extends RecyclerView.Adapter<BlocksAdapter.ViewHolder> {

    public static final String TAG = "BlocksAdapter";

    public static final int VIEW_TYPE_LIST = 0;
    public static final int VIEW_TYPE_SLIDER = 1;
    public static final int VIEW_TYPE_COUNTRY_WIDGET = 2;
    public static final int VIEW_TYPE_NOTICE = 3;

    private List<Block> mBlocks;
    private LayoutInflater mInflater;
    private Context mContext;
    private FragmentManager mFragmentManager;

    public BlocksAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // constructor for home fragment to show country widget fragment
    public BlocksAdapter(Context context, FragmentManager fragmentManager) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFragmentManager = fragmentManager;
    }

    public void setBlocks(List<Block> blocks) {
        mBlocks = blocks;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_COUNTRY_WIDGET:
                Logger.e(TAG,"country widget called");
                BlockCountryWidgetDataBinding widgetBinding = DataBindingUtil.inflate(mInflater,
                        R.layout.view_block_country_widget, parent, false);
                // removed <fragment> tag from view cause of nested fragment issue.
                // Used dynamic fragment inflation as per Android API
                mFragmentManager.beginTransaction()
                        .replace(R.id.country_widget_fragment, CountryWidgetFragment.newInstance(), CountryWidgetFragment.TAG)
                        .commit();
                return new ViewHolder<>(widgetBinding);
            case VIEW_TYPE_NOTICE:
                BlockNoticeDataBinding noticeBinding = DataBindingUtil.inflate(mInflater,
                        R.layout.view_block_notice, parent, false);
                return new ViewHolder<>(noticeBinding);
            case VIEW_TYPE_LIST:
                BlockListDataBinding listBinding = DataBindingUtil.inflate(mInflater,
                        R.layout.view_block_list, parent, false);
                return new ViewHolder<>(listBinding);
            default:
            case VIEW_TYPE_SLIDER:
                BlockSliderDataBinding sliderBinding = DataBindingUtil.inflate(mInflater,
                        R.layout.view_block_slider, parent, false);
                return new ViewHolder<>(sliderBinding);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case BlocksAdapter.VIEW_TYPE_COUNTRY_WIDGET:
                ((BlocksAdapter.ViewHolder<BlockCountryWidgetDataBinding>) holder).mBinding
                        .setBlock(mBlocks.get(position));
                break;
            case BlocksAdapter.VIEW_TYPE_NOTICE:
                ((BlocksAdapter.ViewHolder<BlockNoticeDataBinding>) holder).mBinding
                        .setNotice(mBlocks.get(position).getNotice());
                break;
            case BlocksAdapter.VIEW_TYPE_LIST:
                ((BlocksAdapter.ViewHolder<BlockListDataBinding>) holder).mBinding
                        .setBlock(mBlocks.get(position));
                break;
            default:
            case BlocksAdapter.VIEW_TYPE_SLIDER:
                ((BlocksAdapter.ViewHolder<BlockSliderDataBinding>) holder).mBinding
                        .setBlock(mBlocks.get(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mBlocks.get(position).getLayout().equals("list")) {
            return VIEW_TYPE_LIST;
        } else if (mBlocks.get(position).getLayout().equals("slider")) {
            return VIEW_TYPE_SLIDER;
        } else if (mBlocks.get(position).getLayout().equals("country_widget")) {
            return VIEW_TYPE_COUNTRY_WIDGET;
        } else if (mBlocks.get(position).getLayout().equals("notice")) {
            return VIEW_TYPE_NOTICE;
        }
        return VIEW_TYPE_LIST;
    }

    @Override
    public int getItemCount() {
        return mBlocks == null ? 0 : mBlocks.size();
    }

    public class ViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
        public final T mBinding;

        public ViewHolder(T binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
