package com.taf.shuvayatra.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taf.data.utils.Logger;
import com.taf.model.BaseModel;
import com.taf.model.Category;
import com.taf.model.HeaderItem;
import com.taf.model.Notice;
import com.taf.model.Notification;
import com.taf.model.Podcast;
import com.taf.model.Post;
import com.taf.model.UserInfoModel;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.databinding.ArticleDataBinding;
import com.taf.shuvayatra.databinding.AudioVideoDataBinding;
import com.taf.shuvayatra.databinding.DestinationDataBinding;
import com.taf.shuvayatra.databinding.HeaderDataBinding;
import com.taf.shuvayatra.databinding.InfoListDataBinding;
import com.taf.shuvayatra.databinding.ItemNoticeDataBinding;
import com.taf.shuvayatra.databinding.ItemUserInfoBinding;
import com.taf.shuvayatra.databinding.JourneyCategoryDataBinding;
import com.taf.shuvayatra.databinding.NotificationDataBinding;
import com.taf.shuvayatra.databinding.PlaceDataBinding;
import com.taf.shuvayatra.databinding.PodcastDataBinding;
import com.taf.shuvayatra.ui.interfaces.ListItemClickListener;
import com.taf.util.MyConstants;
import com.taf.util.MyConstants.Adapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class ListAdapter<T extends BaseModel> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    List<T> mDataCollection;
    ListItemClickListener mListener;
    int selectedPosition = -1;
    private Boolean mFromInfo = false;
    private String mDefaultCategory = null;

    public ListAdapter(Context pContext, ListItemClickListener pListener) {
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        this.mListener = pListener;
        mDataCollection = new ArrayList<>();
    }

    public ListAdapter(Context pContext, ListItemClickListener pListener, String pDefaultCategory) {
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        this.mListener = pListener;
        this.mDefaultCategory = pDefaultCategory;
    }

    public ListAdapter(Context pContext, ListItemClickListener pListener, Boolean fromInfo) {
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        this.mListener = pListener;
        mFromInfo = fromInfo;
    }

    public ListAdapter(Context pContext, List<T> pDataCollection, ListItemClickListener pListener) {
        mDataCollection = pDataCollection;
        this.mLayoutInflater = (LayoutInflater) pContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        this.mListener = pListener;
    }

    public List<T> getDataCollection() {
        return mDataCollection;
    }

    public void setDataCollection(List<T> pDataCollection) {
        mDataCollection = pDataCollection;
        notifyDataSetChanged();
    }

    public void addDataCollection(List<T> pDataCollection) {
        mDataCollection.addAll(pDataCollection);
        Logger.d("renderPost-adapter-param", "size:" + pDataCollection.size());
        Logger.d("renderPost-adapter", "size:" + mDataCollection.size());
        int newStartPosition = mDataCollection.size() - pDataCollection.size();
        notifyItemRangeChanged(newStartPosition, pDataCollection.size());
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (mFromInfo) {
            if (viewType == Adapter.TYPE_CATEGORY_HEADER) {
                Logger.e("ListAdapter", "header " + "header category");
                HeaderDataBinding headerDataBinding = DataBindingUtil.inflate
                        (mLayoutInflater, R.layout.view_category_header, parent, false);
                return new HeaderViewHolder(headerDataBinding);
            }
            InfoListDataBinding infoListDataBinding = DataBindingUtil.inflate
                    (mLayoutInflater, R.layout.view_info_list, parent, false);
            return new InfoViewHolder(infoListDataBinding);
        }
        switch (viewType) {
            case Adapter.TYPE_AUDIO:
            case Adapter.TYPE_VIDEO:
                AudioVideoDataBinding vBinding = DataBindingUtil.inflate(mLayoutInflater, R.layout
                        .view_audio_video_list, parent, false);
                viewHolder = new AudioVideoViewHolder(vBinding);
                break;
            case Adapter.TYPE_NEWS:
            case Adapter.TYPE_TEXT:
                ArticleDataBinding articleBinding = DataBindingUtil.inflate(mLayoutInflater, R
                        .layout.view_article, parent, false);
                viewHolder = new ArticleViewHolder(articleBinding);
                break;
            case Adapter.TYPE_JOURNEY_CATEGORY:
                JourneyCategoryDataBinding journeyBinding = DataBindingUtil.inflate(mLayoutInflater,
                        R.layout.view_journey_category_list, parent, false);
                viewHolder = new JourneyCategoryViewHolder(journeyBinding);
                break;
            case Adapter.TYPE_PLACE:
                PlaceDataBinding placeDataBinding = DataBindingUtil.inflate(mLayoutInflater,
                        R.layout.view_place, parent, false);
                viewHolder = new PlaceViewHolder(placeDataBinding);
                break;
            case Adapter.TYPE_PODCAST:
                PodcastDataBinding podcastDataBinding = DataBindingUtil.inflate(mLayoutInflater,
                        R.layout.view_podcast, parent, false);
                viewHolder = new PodcastViewHolder(podcastDataBinding);
                break;
            case Adapter.TYPE_COUNTRY:
                DestinationDataBinding destinationDataBinding = DataBindingUtil.inflate
                        (mLayoutInflater, R.layout.view_destination_list, parent, false);
                viewHolder = new DestinationViewHolder(destinationDataBinding);
                break;
            case Adapter.TYPE_NOTIFICATION:
                NotificationDataBinding notificationDataBinding = DataBindingUtil.inflate
                        (mLayoutInflater, R.layout.view_notification_list, parent, false);
                viewHolder = new NotificationViewHolder(notificationDataBinding);
                break;
            case Adapter.TYPE_USER_INFO:
               ItemUserInfoBinding userInfoDataBinding = DataBindingUtil.inflate(mLayoutInflater,
                        R.layout.item_user_info,parent, false);
                viewHolder = new UserInfoViewHolder(userInfoDataBinding);
                break;
            case Adapter.TYPE_NOTICE:
                ItemNoticeDataBinding noticeDataBinding = DataBindingUtil.inflate(mLayoutInflater,
                        R.layout.item_notice, parent, false);
                viewHolder = new NoticeViewHolder(noticeDataBinding);
                break;
            default:
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mFromInfo) {
            if (holder.getItemViewType() == Adapter.TYPE_CATEGORY_HEADER) {
                ((HeaderViewHolder) holder).mBinding.setTitle(((HeaderItem) mDataCollection.get(position)).getTitle());
                return;
            }
            ((InfoViewHolder) holder).mBinding.setCategory((Category) mDataCollection.get(position));
            return;
        }
        switch (holder.getItemViewType()) {
            case Adapter.TYPE_AUDIO:
            case Adapter.TYPE_VIDEO:
                ((AudioVideoViewHolder) holder).mBinding.setContent((Post) mDataCollection.get
                        (position));
                ((AudioVideoViewHolder) holder).mBinding.setDefaultCategory(mDefaultCategory);
                break;
            case Adapter.TYPE_NEWS:
            case Adapter.TYPE_TEXT:
                ((ArticleViewHolder) holder).mBinding.setArticle((Post) mDataCollection.get
                        (position));
                ((ArticleViewHolder) holder).mBinding.setDefaultCategory(mDefaultCategory);
                break;
            case Adapter.TYPE_JOURNEY_CATEGORY:
                ((JourneyCategoryViewHolder) holder).mBinding.setCategory((Category)
                        mDataCollection.get(position));
                break;
            case Adapter.TYPE_PLACE:
                ((PlaceViewHolder) holder).mBinding.setPlace((Post) mDataCollection.get(position));
                break;
            case Adapter.TYPE_PODCAST:
                ((PodcastViewHolder) holder).mBinding.setPodcast((Podcast) mDataCollection.get
                        (position));
                break;
            case Adapter.TYPE_COUNTRY:
                ((DestinationViewHolder) holder).mBinding.setCategory((Category) mDataCollection
                        .get(position));
                break;
            case Adapter.TYPE_NOTIFICATION:
                ((NotificationViewHolder) holder).mBinding.setNotification((Notification)
                        mDataCollection.get(position));
                ((NotificationViewHolder) holder).mBinding.setSelected(position ==
                        selectedPosition);
                break;
            case Adapter.TYPE_USER_INFO:
                ((UserInfoViewHolder) holder).mBinding.setUser((UserInfoModel) mDataCollection.get(position));
                break;
            case Adapter.TYPE_NOTICE:
                ((NoticeViewHolder) holder).mDataBinding.setNotice((Notice) mDataCollection.get(position));
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataCollection.get(position).getDataType();
    }

    @Override
    public int getItemCount() {
        return (mDataCollection != null) ? mDataCollection.size() : 0;
    }

    public class AudioVideoViewHolder extends RecyclerView.ViewHolder {

        public AudioVideoDataBinding mBinding;

        public AudioVideoViewHolder(AudioVideoDataBinding binding) {
            super(binding.getRoot());
            ButterKnife.bind(this, binding.getRoot());
            this.mBinding = binding;
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onListItemSelected(mBinding.getContent(), getDataCollection()
                            .indexOf(mBinding.getContent()));
                }
            });
        }
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        public ArticleDataBinding mBinding;

        public ArticleViewHolder(ArticleDataBinding binding) {
            super(binding.getRoot());
            ButterKnife.bind(this, binding.getRoot());
            this.mBinding = binding;
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onListItemSelected(mBinding.getArticle(), getDataCollection()
                            .indexOf(mBinding.getArticle()));
                }
            });
        }
    }

    public class JourneyCategoryViewHolder extends RecyclerView.ViewHolder {

        JourneyCategoryDataBinding mBinding;

        public JourneyCategoryViewHolder(JourneyCategoryDataBinding pBinding) {
            super(pBinding.getRoot());
            mBinding = pBinding;
            ButterKnife.bind(this, mBinding.getRoot());
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onListItemSelected(mBinding.getCategory(), getDataCollection()
                            .indexOf(mBinding.getCategory()));
                }
            });
        }

    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {

        PlaceDataBinding mBinding;

        public PlaceViewHolder(PlaceDataBinding pBinding) {
            super(pBinding.getRoot());
            mBinding = pBinding;
            ButterKnife.bind(this, mBinding.getRoot());
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onListItemSelected(mBinding.getPlace(), getDataCollection()
                            .indexOf(mBinding.getPlace()));
                }
            });
        }

    }

    public class DestinationViewHolder extends RecyclerView.ViewHolder {

        DestinationDataBinding mBinding;

        public DestinationViewHolder(DestinationDataBinding pBinding) {
            super(pBinding.getRoot());
            mBinding = pBinding;
            ButterKnife.bind(this, mBinding.getRoot());
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onListItemSelected(mBinding.getCategory(), getDataCollection()
                            .indexOf(mBinding.getCategory()));
                }
            });
        }
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        NotificationDataBinding mBinding;

        public NotificationViewHolder(NotificationDataBinding pBinding) {
            super(pBinding.getRoot());
            mBinding = pBinding;
            ButterKnife.bind(this, mBinding.getRoot());
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getDataCollection().indexOf(mBinding.getNotification());
                    selectedPosition = selectedPosition == position ? -1 : position;
                    notifyDataSetChanged();
                }
            });
        }
    }

    public class InfoViewHolder extends RecyclerView.ViewHolder {

        InfoListDataBinding mBinding;

        public InfoViewHolder(InfoListDataBinding pBinding) {
            super(pBinding.getRoot());
            mBinding = pBinding;
            ButterKnife.bind(this, mBinding.getRoot());
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onListItemSelected(mBinding.getCategory(),
                            getDataCollection().indexOf(mBinding.getCategory()));
                }
            });
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderDataBinding mBinding;

        public HeaderViewHolder(HeaderDataBinding pBinding) {
            super(pBinding.getRoot());
            mBinding = pBinding;
        }
    }

    public class PodcastViewHolder extends RecyclerView.ViewHolder {

        PodcastDataBinding mBinding;

        public PodcastViewHolder(PodcastDataBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onListItemSelected(mBinding.getPodcast(), getDataCollection()
                            .indexOf(mBinding.getPodcast()));
                }
            });
        }

    }

    public class UserInfoViewHolder extends RecyclerView.ViewHolder{

        ItemUserInfoBinding mBinding;
        public UserInfoViewHolder(ItemUserInfoBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    public class NoticeViewHolder extends RecyclerView.ViewHolder{
        ItemNoticeDataBinding mDataBinding;


        public NoticeViewHolder(ItemNoticeDataBinding binding) {
            super(binding.getRoot());

            mDataBinding = binding;

            mDataBinding.dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Notice notice = mDataBinding.getNotice();
                    notice.setFromDismiss(true);
                   mListener.onListItemSelected(mDataBinding.getNotice(),mDataCollection.indexOf(mDataBinding.getNotice()));
                }
            });

            mDataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Notice notice = mDataBinding.getNotice();
                    notice.setFromDismiss(false);

                    mListener.onListItemSelected(mDataBinding.getNotice(),mDataCollection.indexOf(mDataBinding.getNotice()));
                }
            });
        }

    }


}
