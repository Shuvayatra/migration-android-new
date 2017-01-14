package com.taf.shuvayatra.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taf.data.utils.AppPreferences;
import com.taf.data.utils.Logger;
import com.taf.model.BaseModel;
import com.taf.model.Block;
import com.taf.model.Country;
import com.taf.model.CountryWidgetModel;
import com.taf.model.Notice;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.databinding.BlockListDataBinding;
import com.taf.shuvayatra.databinding.BlockNoticeDataBinding;
import com.taf.shuvayatra.databinding.BlockRadioWidgetDataBinding;
import com.taf.shuvayatra.databinding.BlockSliderDataBinding;
import com.taf.shuvayatra.databinding.CountryWidgetDataBinding;
import com.taf.shuvayatra.databinding.ItemCountryInformationDataBinding;
import com.taf.shuvayatra.ui.activity.DestinationDetailActivity;
import com.taf.util.MyConstants;

import java.util.ArrayList;
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

    private AppPreferences mPreferences;

    public BlocksAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPreferences = new AppPreferences(context);
    }

    // constructor for home fragment to show country widget fragment
    public BlocksAdapter(Context context, FragmentManager fragmentManager) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFragmentManager = fragmentManager;
        mPreferences = new AppPreferences(context);
    }

    public List<BaseModel> getBlocks() {
        return mBlocks;
    }

    public void setBlocks(List<BaseModel> blocks) {
        mBlocks = filterNotice(blocks);
        notifyDataSetChanged();
    }

    private List<BaseModel> filterNotice(List<BaseModel> blocks) {
        List<BaseModel> models = new ArrayList<>();
        for (BaseModel block : blocks) {
            if (block instanceof Block) {
                if (((Block) block).getNotice() != null && ((Block) block).getNotice().getId()
                        .equals(mPreferences.getNoticeDismissId())) {
                    continue;
                }
            }
            models.add(block);
        }
        return models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MyConstants.Adapter.TYPE_COUNTRY_WIDGET:
                Logger.e(TAG, "country widget called");
                CountryWidgetDataBinding widgetBinding = DataBindingUtil.inflate(mInflater,
                        R.layout.list_item_country_widget, parent, false);
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
                ((BlocksAdapter.ViewHolder<CountryWidgetDataBinding>) holder).mBinding
                        .setWidgetModel((CountryWidgetModel) mBlocks.get(position));
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
        if (mBlocks.get(position).getDataType() == MyConstants.Adapter.TYPE_BLOCK) {
            Block block = (Block) mBlocks.get(position);
            if (block.getLayout().equalsIgnoreCase(Block.TYPE_LIST)) {
                return MyConstants.Adapter.VIEW_TYPE_LIST;
            } else if (block.getLayout().equalsIgnoreCase(Block.TYPE_SLIDER)) {
                return MyConstants.Adapter.VIEW_TYPE_SLIDER;
            } else if (block.getLayout().equalsIgnoreCase(Block.TYPE_NOTICE)) {
                Logger.e(TAG, "has notice");
                return MyConstants.Adapter.VIEW_TYPE_NOTICE;
            } else if (block.getLayout().equalsIgnoreCase(Block.TYPE_RADIO)) {
                return MyConstants.Adapter.VIEW_TYPE_RADIO_WIDGET;
            }
        } else if (mBlocks.get(position).getDataType() == MyConstants.Adapter.TYPE_COUNTRY ||
                mBlocks.get(position).getDataType() == MyConstants.Adapter.TYPE_COUNTRY_SELECTED) {
            return MyConstants.Adapter.TYPE_COUNTRY;
        } else if (mBlocks.get(position).getDataType() == MyConstants.Adapter.TYPE_COUNTRY_WIDGET) {
            return MyConstants.Adapter.TYPE_COUNTRY_WIDGET;
        }
        return MyConstants.Adapter.VIEW_TYPE_LIST;
    }

    @Override
    public int getItemCount() {
        return mBlocks == null ? 0 : mBlocks.size();
    }

    private String getFormattedDeeplink(String deepLink, List<Long> filterIds) {
        if (filterIds != null && !filterIds.isEmpty()) {
            deepLink += "?category_id=";
//            if(deepLink.equals(""))
//                deepLink = "shuvayatra://feed";
//            deepLink += "?category_id=";
            int index = 0;
            for (Long filterId : filterIds) {
                index++;
                deepLink += filterId;
                if (index < filterIds.size()) {
                    deepLink += ",";
                }
            }
        }
        Logger.e(TAG, "deeplink: " + deepLink);
        return deepLink;
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
                view = ((BlockNoticeDataBinding) mBinding).getRoot();
                ((BlockNoticeDataBinding) mBinding).dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Block block = ((BlockNoticeDataBinding) mBinding).getBlock();
                        mBlocks.remove(block);

                        mPreferences.setNoticeDismissId(block.getNotice().getId());
                        BlocksAdapter.this.notifyItemRemoved(mBlocks.indexOf(block));
                        return;
                    }
                });
            } else if (mBinding instanceof CountryWidgetDataBinding) {
                ((CountryWidgetDataBinding) mBinding).showCountry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "onClick: ");
                        Intent intent = new Intent(mContext, DestinationDetailActivity.class);
                        CountryWidgetModel countryWidgetModel = ((CountryWidgetDataBinding) mBinding).getWidgetModel();

                        intent.putExtra(MyConstants.Extras.KEY_COUNTRY_TITLE, countryWidgetModel.getCountryName());
                        intent.putExtra(MyConstants.Extras.KEY_COUNTRY_ID, countryWidgetModel.getId());
                        mContext.startActivity(intent);
                        return;
                    }
                });
            }

            if (view != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Block block = null;
                        String deepLink = null;
                        if (mBinding instanceof BlockNoticeDataBinding) {
                            block = ((BlockNoticeDataBinding) mBinding).getBlock();
                            deepLink = block.getNotice().getDeeplink();
                        } else {
                            if (mBinding instanceof BlockListDataBinding) {
                                block = ((BlockListDataBinding) mBinding).getBlock();
                            } else if (mBinding instanceof BlockSliderDataBinding) {
                                block = ((BlockSliderDataBinding) mBinding).getBlock();
                            } else if (mBinding instanceof BlockRadioWidgetDataBinding) {
                                mContext.sendBroadcast(new Intent(MyConstants.Intent
                                        .ACTION_SHOW_RADIO));
                                return;
                            }
                            deepLink = block.getDeeplink();
                        }

                        deepLink = getFormattedDeeplink(deepLink, block.getFilterIds());
                        if (deepLink != null && !deepLink.isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(deepLink));
                            intent.putExtra("title", block.getTitle());
                            mContext.startActivity(intent);
                        }
                    }
                });
            }
        }
    }
}
