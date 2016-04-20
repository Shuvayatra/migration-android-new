package com.taf.shuvayatra.util;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.webkit.WebView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

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
}
