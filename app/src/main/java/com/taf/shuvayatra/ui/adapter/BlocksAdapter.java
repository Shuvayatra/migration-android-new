package com.taf.shuvayatra.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taf.data.utils.Logger;
import com.taf.model.BaseModel;
import com.taf.model.Block;
import com.taf.model.Country;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.databinding.BlockCountryWidgetDataBinding;
import com.taf.shuvayatra.databinding.BlockListDataBinding;
import com.taf.shuvayatra.databinding.BlockNoticeDataBinding;
import com.taf.shuvayatra.databinding.BlockRadioWidgetDataBinding;
import com.taf.shuvayatra.databinding.BlockSliderDataBinding;
import com.taf.shuvayatra.databinding.ItemCountryInformationDataBinding;
import com.taf.shuvayatra.ui.fragment.CountryWidgetFragment;
import com.taf.util.MyConstants;

import java.util.List;

/**
 * Created by julian on 10/18/16.
 */

public class BlocksAdapter extends RecyclerView.Adapter<BlocksAdapter.ViewHolder> {

    public static final String TAG = "BlocksAdapter";

    private List<BaseModel> mBlocks;
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

    public void setBlocks(List<BaseModel> blocks) {
        mBlocks = blocks;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MyConstants.Adapter.TYPE_COUNTRY_WIDGET:
                Logger.e(TAG, "country widget called");
                BlockCountryWidgetDataBinding widgetBinding = DataBindingUtil.inflate(mInflater,
                        R.layout.view_block_country_widget, parent, false);
                // removed <fragment> tag from view cause of nested fragment issue.
                // Used dynamic fragment inflation as per Android API
                mFragmentManager.beginTransaction()
                        .replace(R.id.country_widget_fragment, CountryWidgetFragment.newInstance(), CountryWidgetFragment.TAG)
                        .commit();
                return new ViewHolder<>(widgetBinding);
            case MyConstants.Adapter.VIEW_TYPE_NOTICE:
                BlockNoticeDataBinding noticeBinding = DataBindingUtil.inflate(mInflater,
                        R.layout.view_block_notice, parent, false);
                return new ViewHolder<>(noticeBinding);
            case MyConstants.Adapter.VIEW_TYPE_LIST:
                BlockListDataBinding listBinding = DataBindingUtil.inflate(mInflater,
                        R.layout.view_block_list, parent, false);
                return new ViewHolder<>(listBinding);
            case MyConstants.Adapter.VIEW_TYPE_RADIO_WIDGET:
                BlockRadioWidgetDataBinding radioBinding = DataBindingUtil.inflate(mInflater, R
                        .layout.view_block_radio_widget, parent, false);
                return new ViewHolder<>(radioBinding);
            default:
            case MyConstants.Adapter.VIEW_TYPE_SLIDER:
                BlockSliderDataBinding sliderBinding = DataBindingUtil.inflate(mInflater,
                        R.layout.view_block_slider, parent, false);
                return new ViewHolder<>(sliderBinding);
            case MyConstants.Adapter.TYPE_COUNTRY:
                ItemCountryInformationDataBinding countryBinding = DataBindingUtil.inflate(mInflater,
                        R.layout.item_country_information, parent, false);
                return new ViewHolder<>(countryBinding);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case MyConstants.Adapter.TYPE_COUNTRY:
                ((ViewHolder<ItemCountryInformationDataBinding>) holder).mBinding
                        .setCountry((Country) mBlocks.get(position));
                break;
            case MyConstants.Adapter.TYPE_COUNTRY_WIDGET:
                ((BlocksAdapter.ViewHolder<BlockCountryWidgetDataBinding>) holder).mBinding
                        .setModel(mBlocks.get(position));
                break;
            case MyConstants.Adapter.VIEW_TYPE_NOTICE:
                ((BlocksAdapter.ViewHolder<BlockNoticeDataBinding>) holder).mBinding
                        .setBlock((Block) mBlocks.get(position));
                break;
            case MyConstants.Adapter.VIEW_TYPE_LIST:
                ((BlocksAdapter.ViewHolder<BlockListDataBinding>) holder).mBinding
                        .setBlock((Block) mBlocks.get(position));
                break;
            case MyConstants.Adapter.VIEW_TYPE_RADIO_WIDGET:
                ((ViewHolder<BlockRadioWidgetDataBinding>) holder).mBinding
                        .setBlock((Block) mBlocks.get(position));
                break;
            default:
            case MyConstants.Adapter.VIEW_TYPE_SLIDER:
                ((BlocksAdapter.ViewHolder<BlockSliderDataBinding>) holder).mBinding
                        .setBlock((Block) mBlocks.get(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mBlocks.get(position).getDataType() == MyConstants.Adapter.TYPE_BLOCK) {
            Block block = (Block) mBlocks.get(position);
            if (block.getLayout().equals("list")) {
                return MyConstants.Adapter.VIEW_TYPE_LIST;
            } else if (block.getLayout().equals("slider")) {
                return MyConstants.Adapter.VIEW_TYPE_SLIDER;
            }else if (block.getLayout().equals("notice")) {
                Logger.e(TAG,"has notice");
                return MyConstants.Adapter.VIEW_TYPE_NOTICE;
            } else if (block.getLayout().equals("radio_widget")) {
                return MyConstants.Adapter.VIEW_TYPE_RADIO_WIDGET;
            }
        } else if(mBlocks.get(position).getDataType() == MyConstants.Adapter.TYPE_COUNTRY ||
                mBlocks.get(position).getDataType() == MyConstants.Adapter.TYPE_COUNTRY_SELECTED){
            return MyConstants.Adapter.TYPE_COUNTRY;
        } else if(mBlocks.get(position).getDataType() == MyConstants.Adapter.TYPE_COUNTRY_WIDGET){
            return MyConstants.Adapter.TYPE_COUNTRY_WIDGET;
        }
        return MyConstants.Adapter.VIEW_TYPE_LIST;
    }

    @Override
    public int getItemCount() {
        return mBlocks == null ? 0 : mBlocks.size();
    }

    private String getFormattedDeeplink(Block block) {
        // TODO: 10/27/16 remove check for radio widget after radio channel implementation
        if (block.getLayout().equals("radio_widget")) {
            return "shuvayatra://podcasts";
        }
        String deeplink = block.getDeeplink();
        if (block.getFilterIds() != null && !block.getFilterIds().isEmpty()) {
            deeplink += "?category_id=";
            int index = 0;
            for (Long filterId : block.getFilterIds()) {
                index++;
                deeplink += filterId;
                if (index < block.getFilterIds().size()) {
                    deeplink += ",";
                }
            }
        }
        return deeplink;
    }

    public class ViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
        public final T mBinding;

        public ViewHolder(T binding) {
            super(binding.getRoot());
            mBinding = binding;

            View view = null;
            if (mBinding instanceof BlockListDataBinding) {
                view = ((BlockListDataBinding) mBinding).deeplink;
            } else if (mBinding instanceof BlockSliderDataBinding) {
                view = ((BlockSliderDataBinding) mBinding).deeplink;
            } else if (mBinding instanceof BlockRadioWidgetDataBinding) {
                view = ((BlockRadioWidgetDataBinding) mBinding).play;
            } else if (mBinding instanceof BlockNoticeDataBinding) {
                view = ((BlockNoticeDataBinding) mBinding).dismiss;
            }

            if (view != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Block block = null;
                        if (mBinding instanceof BlockNoticeDataBinding) {
                            block = ((BlockNoticeDataBinding) mBinding).getBlock();
                            mBlocks.remove(block);
                            BlocksAdapter.this.notifyItemRemoved(mBlocks.indexOf(block));
                            return;
                        } else if (mBinding instanceof BlockListDataBinding) {
                            block = ((BlockListDataBinding) mBinding).getBlock();
                        } else if (mBinding instanceof BlockSliderDataBinding) {
                            block = ((BlockSliderDataBinding) mBinding).getBlock();
                        } else if (mBinding instanceof BlockRadioWidgetDataBinding) {
                            block = ((BlockRadioWidgetDataBinding) mBinding).getBlock();
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(getFormattedDeeplink(block)));
                        intent.putExtra("title", block.getTitle());
                        mContext.startActivity(intent);
                    }
                });
            }
        }
    }
}
