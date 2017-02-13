package com.taf.shuvayatra.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taf.data.utils.AppPreferences;
import com.taf.data.utils.Logger;
import com.taf.model.BaseModel;
import com.taf.model.Block;
import com.taf.model.Country;
import com.taf.model.CountryWidgetModel;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.databinding.BlockListDataBinding;
import com.taf.shuvayatra.databinding.BlockNoticeDataBinding;
import com.taf.shuvayatra.databinding.BlockRadioWidgetDataBinding;
import com.taf.shuvayatra.databinding.BlockSliderDataBinding;
import com.taf.shuvayatra.databinding.CountryWidgetDataBinding;
import com.taf.shuvayatra.databinding.ItemCountryInformationDataBinding;
import com.taf.shuvayatra.ui.interfaces.BlockItemAnalytics;
import com.taf.shuvayatra.ui.interfaces.ListItemClickWithDataTypeListener;
import com.taf.shuvayatra.ui.views.BlockViewHolder;
import com.taf.util.MyConstants;
import com.taf.util.MyConstants.Adapter;

import java.util.ArrayList;
import java.util.List;

import static com.taf.util.MyConstants.Adapter.TYPE_COUNTRY_WIDGET;
import static com.taf.util.MyConstants.Adapter.VIEW_TYPE_LIST;
import static com.taf.util.MyConstants.Adapter.VIEW_TYPE_NOTICE;
import static com.taf.util.MyConstants.Adapter.VIEW_TYPE_RADIO_WIDGET;
import static com.taf.util.MyConstants.Adapter.VIEW_TYPE_SLIDER;

/**
 * Created by julian on 10/18/16.
 */

public class BlocksAdapter extends RecyclerView.Adapter<BlockViewHolder> {

    public static final String TAG = "BlocksAdapter";

    private List<BaseModel> mBlocks;
    private LayoutInflater mInflater;
    private Context mContext;

    private AppPreferences mPreferences;
    private ListItemClickWithDataTypeListener mCallback;
    private BlockItemAnalytics analyticsCallback;

    public BlocksAdapter(Context context, ListItemClickWithDataTypeListener callback, BlockItemAnalytics analyticsCallback) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPreferences = new AppPreferences(context);
        mCallback = callback;
        this.analyticsCallback = analyticsCallback;
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
        // added null check for config changes
        if (items != null)
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
    public BlockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {

            case TYPE_COUNTRY_WIDGET:
                CountryWidgetDataBinding widgetBinding = DataBindingUtil.inflate(mInflater,
                        R.layout.list_item_country_widget, parent, false);
                return new CountryWidgetViewHolder(widgetBinding);

            case Adapter.VIEW_TYPE_NOTICE:
                BlockNoticeDataBinding noticeBinding = DataBindingUtil.inflate(mInflater,
                        R.layout.view_block_notice, parent, false);
                return new NoticeViewHolder(noticeBinding);

            case VIEW_TYPE_LIST:
                BlockListDataBinding listBinding = DataBindingUtil.inflate(mInflater,
                        R.layout.view_block_list, parent, false);
                return new ListViewHolder(listBinding);

            case Adapter.VIEW_TYPE_RADIO_WIDGET:
                BlockRadioWidgetDataBinding radioBinding = DataBindingUtil.inflate(mInflater, R
                        .layout.view_block_radio_widget, parent, false);
                return new RadioWidgetViewHolder(radioBinding);

            case Adapter.VIEW_TYPE_SLIDER:
                BlockSliderDataBinding sliderBinding = DataBindingUtil.inflate(mInflater,
                        R.layout.view_block_slider, parent, false);
                return new SliderViewHolder(sliderBinding);

            default:
            case Adapter.TYPE_COUNTRY:
                ItemCountryInformationDataBinding countryBinding = DataBindingUtil.inflate(mInflater,
                        R.layout.item_country_information, parent, false);
                return new CountryInformationViewHolder(countryBinding);

        }
    }

    @Override
    public void onBindViewHolder(BlockViewHolder holder, int position) {
        holder.setModel(mBlocks.get(position));
    }

    @Override
    public long getItemId(int position) {
        return mBlocks.get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        if (mBlocks.get(position).getDataType() == Adapter.TYPE_BLOCK) {
            Block block = (Block) mBlocks.get(position);
            if (block.getLayout().equalsIgnoreCase(Block.TYPE_LIST)) {
                return VIEW_TYPE_LIST;
            } else if (block.getLayout().equalsIgnoreCase(Block.TYPE_SLIDER)) {
                return Adapter.VIEW_TYPE_SLIDER;
            } else if (block.getLayout().equalsIgnoreCase(Block.TYPE_NOTICE)) {
                return Adapter.VIEW_TYPE_NOTICE;
            } else if (block.getLayout().equalsIgnoreCase(Block.TYPE_RADIO)) {
                return Adapter.VIEW_TYPE_RADIO_WIDGET;
            }
        } else if (mBlocks.get(position).getDataType() == Adapter.TYPE_COUNTRY ||
                mBlocks.get(position).getDataType() == Adapter.TYPE_COUNTRY_SELECTED) {
            return Adapter.TYPE_COUNTRY;
        } else if (mBlocks.get(position).getDataType() == TYPE_COUNTRY_WIDGET) {
            return TYPE_COUNTRY_WIDGET;
        } else if (mBlocks.get(position).getDataType() == Adapter.TYPE_NOTICE) {
            return Adapter.VIEW_TYPE_NOTICE;
        }
        return VIEW_TYPE_LIST;
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

        String location = mPreferences.getLocation();
        if (!location.equalsIgnoreCase(MyConstants.Preferences.DEFAULT_LOCATION)
                && !location.equalsIgnoreCase(mContext.getString(R.string.country_not_decided_yet))) {
            Country country = Country.makeCountryFromPreference(mPreferences.getLocation());
            String countryId = String.valueOf(country.getId());
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

    public class ListViewHolder extends BlockViewHolder<BlockListDataBinding, Block> {

        public ListViewHolder(BlockListDataBinding binding) {
            super(binding);

            binding.deeplink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String deeplink;
                    deeplink = getModel().getDeeplink();
                    deeplink = getFormattedDeepLink(deeplink, getModel().getFilterIds());
                    mCallback.onDeeplinkSelected(deeplink, getModel(), getDataType(), getAdapterPosition());
                }
            });
        }

        @Override
        public Block getModel() {
            return getBinding().getBlock();
        }

        @Override
        public void setModel(Block model) {
            getBinding().setBlock(model);
            getBinding().setCallback(analyticsCallback);
        }

        @Override
        public int getDataType() {
            return VIEW_TYPE_LIST;
        }
    }

    public class SliderViewHolder extends BlockViewHolder<BlockSliderDataBinding, Block> {

        public SliderViewHolder(BlockSliderDataBinding binding) {
            super(binding);

            binding.deeplink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String deeplink;
                    deeplink = getModel().getDeeplink();
                    deeplink = getFormattedDeepLink(deeplink, getModel().getFilterIds());
                    mCallback.onDeeplinkSelected(deeplink, getModel(), getDataType(),
                            getAdapterPosition());
                }
            });
        }

        @Override
        public Block getModel() {
            return getBinding().getBlock();
        }

        @Override
        public void setModel(Block model) {
            getBinding().setBlock(model);
            getBinding().setCallback(analyticsCallback);
        }

        @Override
        public int getDataType() {
            return VIEW_TYPE_SLIDER;
        }
    }

    private class RadioWidgetViewHolder extends BlockViewHolder<BlockRadioWidgetDataBinding, Block> {

        public RadioWidgetViewHolder(BlockRadioWidgetDataBinding binding) {
            super(binding);

            binding.radioContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onListItemSelected(getModel(), getDataType(), getAdapterPosition());
                }
            });
        }

        @Override
        public Block getModel() {
            return getBinding().getBlock();
        }

        @Override
        public void setModel(Block model) {
            getBinding().setBlock(model);
        }

        @Override
        public int getDataType() {
            return VIEW_TYPE_RADIO_WIDGET;
        }
    }

    private class NoticeViewHolder extends BlockViewHolder<BlockNoticeDataBinding, Block> {

        public NoticeViewHolder(BlockNoticeDataBinding binding) {
            super(binding);

            binding.dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onListItemSelected(getModel(), getDataType(), getAdapterPosition());
                }
            });

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (getModel().getNotice() != null && getModel().getNotice().getDeeplink() != null
                            && !getModel().getNotice().getDeeplink().isEmpty()) {

                        String deeplink = getFormattedDeepLink(getModel().getNotice().getDeeplink(),
                                getModel().getFilterIds());
                        mCallback.onDeeplinkSelected(deeplink, getModel(), getDataType(), getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public Block getModel() {
            return getBinding().getBlock();
        }

        @Override
        public void setModel(Block model) {
            getBinding().setBlock(model);
        }

        @Override
        public int getDataType() {
            return VIEW_TYPE_NOTICE;
        }
    }

    private class CountryWidgetViewHolder extends BlockViewHolder<CountryWidgetDataBinding, CountryWidgetModel> {

        public CountryWidgetViewHolder(CountryWidgetDataBinding binding) {
            super(binding);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onListItemSelected(getModel(), getDataType(), getAdapterPosition());
                }
            });
        }

        @Override
        public CountryWidgetModel getModel() {
            return getBinding().getWidgetModel();
        }

        @Override
        public void setModel(CountryWidgetModel model) {
            getBinding().setWidgetModel(model);
        }

        @Override
        public int getDataType() {
            return TYPE_COUNTRY_WIDGET;
        }
    }

    private class CountryInformationViewHolder extends BlockViewHolder<ItemCountryInformationDataBinding, Country> {

        public CountryInformationViewHolder(ItemCountryInformationDataBinding binding) {
            super(binding);
        }

        @Override
        public Country getModel() {
            return getBinding().getCountry();
        }

        @Override
        public void setModel(Country model) {
            getBinding().setCountry(model);
        }

        @Override
        public int getDataType() {
            return Adapter.TYPE_COUNTRY;
        }
    }
}
