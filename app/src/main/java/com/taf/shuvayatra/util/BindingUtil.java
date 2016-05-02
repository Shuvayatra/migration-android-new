package com.taf.shuvayatra.util;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.databinding.AudioVideoDataBinding;
import com.taf.shuvayatra.databinding.PlaceDataBinding;
import com.taf.shuvayatra.ui.activity.AudioDetailActivity;
import com.taf.shuvayatra.ui.activity.PlacesDetailActivity;
import com.taf.shuvayatra.ui.activity.VideoDetailActivity;
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

    @BindingAdapter("bind:elapsedTime")
    public static void setElapsedTime(TextView pView, Long millis) {
        pView.setText(getTimeAgo(millis));
    }

    @BindingAdapter("bind:similarPosts")
    public static void setSimilarPosts(LinearLayout pContainer, List<Post> pPosts) {
        if (pPosts != null && pPosts.size() > 0) {
            for (Post post : pPosts) {
                if (post.getDataType() == MyConstants.Adapter.TYPE_AUDIO || post.getDataType() ==
                        MyConstants.Adapter.TYPE_AUDIO) {
                    showSimilarAudioVideo(pContainer.getContext(), pContainer, post);
                } else if (post.getDataType() == MyConstants.Adapter.TYPE_PLACE) {
                    showSimilarPlace(pContainer.getContext(), pContainer, post);
                }
            }
        }
    }

    public static void showSimilarAudioVideo(final Context pContext, LinearLayout pContainer, final Post
            pPost) {
        if (pPost != null) {
            AudioVideoDataBinding audioDataBinding = DataBindingUtil.inflate
                    (LayoutInflater.from(pContext), R.layout.view_audio_video_list, pContainer,
                            false);
            audioDataBinding.setContent(pPost);
            View view = audioDataBinding.getRoot();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(pContext, (pPost.getDataType() == MyConstants.Adapter
                            .TYPE_AUDIO) ? AudioDetailActivity.class : VideoDetailActivity.class);
                    intent.putExtra((pPost.getDataType() == MyConstants.Adapter.TYPE_AUDIO) ?
                            MyConstants.Extras.KEY_AUDIO : MyConstants.Extras.KEY_VIDEO, pPost);
                    pContext.startActivity(intent);
                }
            });
            pContainer.addView(view);
        }
    }

    public static void showSimilarPlace(final Context pContext, LinearLayout pContainer, final Post
            pPost) {
        if (pPost != null) {
            PlaceDataBinding placeDataBinding = DataBindingUtil.inflate(LayoutInflater.from
                    (pContext), R.layout.view_place, pContainer, false);
            placeDataBinding.setPlace(pPost);
            View view = placeDataBinding.getRoot();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(pContext, PlacesDetailActivity.class);
                    intent.putExtra(MyConstants.Extras.KEY_PLACE, pPost);
                    pContext.startActivity(intent);
                }
            });
            pContainer.addView(view);
        }
    }

    public static String getTimeAgo(long time) {
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
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else if (diff < MONTH_MILLIS) {
            return diff / DAY_MILLIS + " days ago";
        } else {
            return diff / MONTH_MILLIS + " months ago";
        }
    }
}
