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
import com.taf.shuvayatra.ui.activity.DeepLinkActivity;
import com.taf.shuvayatra.ui.activity.DestinationDetailActivity;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
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

    private AppPreferences mPreferences;

    public BlocksAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPreferences = new AppPreferences(context);
    }

    public List<BaseModel> getBlocks() {
        return mBlocks;
    }

    public void setBlocks(List<BaseModel> blocks) {
        mBlocks = removeDismissedNotice(blocks);
        notifyDataSetChanged();
    }

    private List<BaseModel> removeDismissedNotice(List<BaseModel> items) {
        List<BaseModel> baseModels = new ArrayList<>();
        for (BaseModel item : items) {
            baseModels.add(item);

            if (item instanceof Block) {
                if (((Block) item).getLayout().equalsIgnoreCase(Block.TYPE_NOTICE) &&
                        mPreferences.getNoticeDismissId().contains(((Block) item).getNotice().getId()
                                .toString())) {
                    baseModels.remove(item);
                }
            }
        }
        return baseModels;
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
        } else if (mBlocks.get(position).getDataType() == MyConstants.Adapter.TYPE_NOTICE) {
            return MyConstants.Adapter.VIEW_TYPE_NOTICE;
        }
        return MyConstants.Adapter.VIEW_TYPE_LIST;
    }

    @Override
    public int getItemCount() {
        return mBlocks == null ? 0 : mBlocks.size();
    }

    private String getFormattedDeepLink(String deepLink, List<Long> filterIds) {

        if (filterIds != null && !filterIds.isEmpty()) {
            deepLink += "?category_id=";
            int index = 0;
            for (Long filterId : filterIds) {
                index++;
                deepLink += filterId;
                if (index < filterIds.size()) {
                    deepLink += ",";
                }
            }
        }

        if (!mPreferences.getLocation().equalsIgnoreCase(MyConstants.Preferences
                .DEFAULT_LOCATION) && !mPreferences.getLocation()
                .equalsIgnoreCase(mContext.getString(R.string.country_not_decided_yet))) {
            String countryId = mPreferences.getLocation().split(",")[0];
            Logger.e(TAG, ">>> country id: " + countryId);
            deepLink += "&country_id=" + countryId;
        }

        if (mPreferences.getGender() != null) {
            String gender = mPreferences.getGender().equalsIgnoreCase(mContext.getString(R.string.gender_other)) ? "O" :
                    mPreferences.getGender().equalsIgnoreCase(mContext.getString(R.string.gender_male)) ? "M"
                            : "F";
            deepLink += "&gender=" + gender;
        }

        Logger.e(TAG, "deeplink: " + deepLink);
        return deepLink;
    }

    public class ViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
        public final T mBinding;

        public ViewHolder(final T binding) {
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

                mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Block block = ((BlockNoticeDataBinding) mBinding).getBlock();
                        if (block.getNotice().getDeeplink() != null
                                && !block.getNotice().getDeeplink().isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(block.getNotice().getDeeplink()));
                            intent.putExtra("title", block.getNotice().getTitle());
                            mContext.startActivity(intent);
                        }
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

                        deepLink = getFormattedDeepLink(deepLink, block.getFilterIds());
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
