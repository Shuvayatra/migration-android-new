package com.taf.shuvayatra.util;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.webkit.WebView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taf.data.utils.Logger;

public class BindingUtil {
    @BindingAdapter("bind:imageUrl")
    public static void setImage(SimpleDraweeView pView, String url) {
        if (url != null) {
            /*ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                    .setProgressiveRenderingEnabled(true)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(pView.getController())
                    .build();
            pView.setController(controller);*/
            Logger.e("BindingUtil", "url:"+url);
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

    public static String getTimeAgo(long time) {
        final int SECOND_MILLIS = 1000;
        final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        final int DAY_MILLIS = 24 * HOUR_MILLIS;

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
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }
}
