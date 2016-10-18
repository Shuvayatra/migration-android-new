package com.taf.shuvayatra.util;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taf.data.utils.Logger;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.databinding.AudioVideoDataBinding;
import com.taf.shuvayatra.databinding.PhoneNumberDataBinding;
import com.taf.shuvayatra.databinding.PhoneNumberLargeDataBinding;
import com.taf.shuvayatra.databinding.PlaceDataBinding;
import com.taf.shuvayatra.ui.deprecated.activity.PlacesDetailActivity;
import com.taf.shuvayatra.ui.deprecated.activity.VideoDetailActivity;
import com.taf.shuvayatra.ui.deprecated.interfaces.ListItemClickListener;
import com.taf.util.MyConstants;

import java.util.List;

public class BindingUtil {
    @BindingAdapter("bind:imageUrl")
    public static void setImage(SimpleDraweeView pView, String url) {
        if (url != null) {
            pView.setImageURI(Uri.parse(url));
        }
    }

    @BindingAdapter("bind:mediaDuration")
    public static void setDuration(TextView pView, String duration) {
        if (duration != null) {
            String[] data = duration.split(":");
            if (data[0].equals("00")) {
                pView.setText(data[1] + ":" + data[2]);
            } else {
                pView.setText(duration);
            }
        }
    }

    @BindingAdapter("bind:htmlContent")
    public static void setHtmlContent(WebView pView, String content) {
        StringBuilder sb = new StringBuilder();
        sb.append("<HTML><HEAD><LINK href=\"styles.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>");
        sb.append(content);
        sb.append("</body></HTML>");
        pView.loadDataWithBaseURL("file:///android_asset/", sb.toString(), "text/html",
                "utf-8", null);
    }

    @BindingAdapter("bind:address")
    public static void setAddress(final TextView pView, final String pAddress) {
        if (pAddress == null || pAddress.isEmpty()) {
            pView.setText("");
        } else {
            pView.setText(pAddress);
            pView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("geo:0,0?q=" + pAddress));
                    if (intent.resolveActivity(pView.getContext().getPackageManager()) != null) {
                        pView.getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    @BindingAdapter("bind:elapsedTime")
    public static void setElapsedTime(TextView pView, Long elapsedTime) {
        Logger.e("BindingUtil", "createdAt: " + elapsedTime);
        if (elapsedTime != null){
            pView.setText(getTimeAgo(elapsedTime, pView.getContext()));
        }
    }

    @BindingAdapter("bind:similarPosts")
    public static void setSimilarPosts(LinearLayout pContainer, List<Post> pPosts) {
        setSimilarPosts(pContainer, pPosts, null, null);
    }

    @BindingAdapter({"bind:similarPosts", "bind:clickListener"})
    public static void setSimilarPosts(LinearLayout pContainer, List<Post> pPosts,
                                       ListItemClickListener pListener) {
        setSimilarPosts(pContainer, pPosts, null, pListener);
    }

    @BindingAdapter({"bind:similarPosts", "bind:countryId"})
    public static void setSimilarPosts(LinearLayout pContainer, List<Post> pPosts, Long
            countryId) {
        setSimilarPosts(pContainer, pPosts, countryId, null);
    }

    private static void setSimilarPosts(LinearLayout pContainer, List<Post> pPosts, Long
            countryId, ListItemClickListener pListener) {
        pContainer.removeAllViews();
        if (pPosts != null && pPosts.size() > 0) {
            for (Post post : pPosts) {
                if (post.getDataType() == MyConstants.Adapter.TYPE_AUDIO || post.getDataType() ==
                        MyConstants.Adapter.TYPE_VIDEO) {
                    showSimilarAudioVideo(pContainer.getContext(), pContainer, post, pListener);
                } else if (post.getDataType() == MyConstants.Adapter.TYPE_PLACE) {
                    showSimilarPlace(pContainer.getContext(), pContainer, post, countryId);
                }
            }
        }
    }

    @BindingAdapter("bind:phoneNumbers")
    public static void setPhoneNumbers(LinearLayout pContainer, List<String> pNumbers) {
        pContainer.removeAllViews();
        if (pNumbers != null && !pNumbers.isEmpty()) {
            for (String number : pNumbers) {
                showPhoneNumber(pContainer.getContext(), pContainer, number, false);
            }
        }
    }

    @BindingAdapter("bind:phoneNumbersBig")
    public static void setPhoneNumbersBig(LinearLayout pContainer, List<String> pNumbers) {
        pContainer.removeAllViews();
        if (pNumbers != null && !pNumbers.isEmpty()) {
            for (String number : pNumbers) {
                showPhoneNumber(pContainer.getContext(), pContainer, number, true);
            }
        }
    }

    public static void showPhoneNumber(final Context pContext, final LinearLayout pContainer,
                                       final String pNumber, boolean isBig) {
        if (pNumber != null && !pNumber.isEmpty()) {
            ViewDataBinding dataBinding;
            if (isBig) {
                dataBinding = DataBindingUtil.inflate(LayoutInflater.from
                        (pContext), R.layout.view_phone_numbers_large, pContainer, false);
                ((PhoneNumberLargeDataBinding) dataBinding).setNumber(pNumber);
            } else {
                dataBinding = DataBindingUtil.inflate(LayoutInflater.from
                        (pContext), R.layout.view_phone_numbers, pContainer, false);
                ((PhoneNumberDataBinding) dataBinding).setNumber(pNumber);
            }
            View view = dataBinding.getRoot();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + pNumber));
                    pContext.startActivity(callIntent);
                }
            });
            pContainer.addView(view);
        }
    }

    public static void showSimilarAudioVideo(final Context pContext, LinearLayout pContainer, final Post
            pPost, final ListItemClickListener pListener) {
        if (pPost != null) {
            Logger.e("BindingUtil", "category");
            AudioVideoDataBinding audioDataBinding = DataBindingUtil.inflate
                    (LayoutInflater.from(pContext), R.layout.view_audio_video_list, pContainer,
                            false);
            audioDataBinding.setContent(pPost);
            View view = audioDataBinding.getRoot();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, pContext.getResources().getDimensionPixelOffset(R.dimen
                    .spacing_small));
            view.setLayoutParams(params);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pPost.getDataType() == MyConstants.Adapter.TYPE_VIDEO) {
                        Intent intent = new Intent(pContext, VideoDetailActivity.class);
                        intent.putExtra(MyConstants.Extras.KEY_VIDEO, pPost);
                        pContext.startActivity(intent);
                    } else {
                        pListener.onListItemSelected(pPost, -1);
                    }
                }
            });
            pContainer.addView(view);
        }
    }

    public static void showSimilarPlace(final Context pContext, LinearLayout pContainer, final Post
            pPost, final Long countryId) {
        if (pPost != null) {
            PlaceDataBinding placeDataBinding = DataBindingUtil.inflate(LayoutInflater.from
                    (pContext), R.layout.view_place, pContainer, false);
            placeDataBinding.setPlace(pPost);
            View view = placeDataBinding.getRoot();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, pContext.getResources().getDimensionPixelOffset(R.dimen
                    .spacing_small));
            view.setLayoutParams(params);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(pContext, PlacesDetailActivity.class);
                    intent.putExtra(MyConstants.Extras.KEY_PLACE, pPost);
                    intent.putExtra(MyConstants.Extras.KEY_CATEGORY_ID, countryId);
                    pContext.startActivity(intent);
                }
            });
            pContainer.addView(view);
        }
    }

    public static String getTimeAgo(long time, Context pContext) {
        final long SECOND_MILLIS = 1000;
        final long MINUTE_MILLIS = 60 * SECOND_MILLIS;
        final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
        final long DAY_MILLIS = 24 * HOUR_MILLIS;
        final long MONTH_MILLIS = 30 * DAY_MILLIS;

        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return pContext.getString(R.string.just_now);
        } else if (diff < 2 * MINUTE_MILLIS) {
            return pContext.getString(R.string.a_minute_ago);
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " " + pContext.getString(R.string.minute_ago);
        } else if (diff < 90 * MINUTE_MILLIS) {
            return pContext.getString(R.string.a_hour_ago);
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " " + pContext.getString(R.string.hour_ago);
        } else if (diff < 48 * HOUR_MILLIS) {
            return pContext.getString(R.string.yesterday);
        } else if (diff < MONTH_MILLIS) {
            return diff / DAY_MILLIS + " " + pContext.getString(R.string.days_ago);
        } else {
            return diff / MONTH_MILLIS + " " + pContext.getString(R.string.months_ago);
        }
    }
}
