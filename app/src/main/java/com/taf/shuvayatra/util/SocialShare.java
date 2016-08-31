package com.taf.shuvayatra.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.taf.model.Post;

import java.util.List;

/**
 * Created by asim on 8/17/16.
 */
public class SocialShare {

    private PackageManager packageManager;
    private static   String shareSuffix=" - Shared via Shubhayatra App";

    public SocialShare(Context context){
        packageManager = context.getPackageManager();
    }

    public  Intent getFacebookIntent(Post post) {
        Intent intent = getShareIntent("katana",post);
        if(post!=null){
            Uri uri =Uri.parse(post.getFeaturedImage());
            intent.putExtra(Intent.EXTRA_TITLE,post.getTitle()+" "+shareSuffix);
            intent.putExtra(Intent.EXTRA_TEXT,post.getShareUrl()+" "+shareSuffix);
        }
            intent.setType("text/plain");
        if (intent != null) {
            return intent;
        }
        return null;
    }

    public  Intent getTwitterIntent(Post post) {
        Intent intent = getShareIntent("twitter",post);
        if(post!=null){
            Uri uri =Uri.parse(post.getFeaturedImage());
            intent.putExtra(Intent.EXTRA_TITLE,post.getTitle() + " " + shareSuffix);
            intent.putExtra(Intent.EXTRA_TEXT,post.getTitle()+" - "+post.getShareUrl()+" "+shareSuffix);
        }
            intent.setType("text/plain");
        if (intent != null) {
            return intent;
        }
        return null;

    }

    public  Intent getViberIntent(Post post) {
        Intent intent = getShareIntent("viber",post);
        if(post!=null){
            Uri uri =Uri.parse(post.getFeaturedImage());
            intent.putExtra(Intent.EXTRA_TITLE,post.getTitle() + " " + shareSuffix);
            intent.putExtra(Intent.EXTRA_TEXT,post.getTitle()+" - "+post.getShareUrl()+" "+shareSuffix);
            //intent.putExtra(Intent.EXTRA_STREAM, uri);
        }
            intent.setType("text/plain");
        if (intent != null) {
            return intent;
        }
        return null;
    }

    public  Intent getSmsIntent(Post post) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        sendIntent.putExtra("sms_body", post.getTitle()+" - "+post.getShareUrl()+" "+shareSuffix);
        sendIntent.putExtra(Intent.EXTRA_TEXT, post.getTitle()+" - "+post.getShareUrl()+" "+shareSuffix);
        sendIntent.putExtra(Intent.EXTRA_TITLE, post.getTitle());
        return sendIntent;
    }

    public  Intent getWhatsApp(Post post) {
        Intent intent = getShareIntent("whatsapp",post);
        if(post!=null) {
            Uri uri = Uri.parse(post.getFeaturedImage());
            intent.putExtra(Intent.EXTRA_TITLE, post.getTitle());
            intent.putExtra(Intent.EXTRA_TEXT, post.getTitle()+" - "+post.getShareUrl()+" "+shareSuffix);
        }
            intent.setType("text/plain");
        if (intent != null) {
            return intent;
        }
        return null;
    }
    public  Intent getGenericShare(Post post) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
        sendIntent.putExtra(Intent.EXTRA_TEXT, post.getTitle()+" - "+post.getShareUrl()+" "+shareSuffix);
        sendIntent.putExtra(Intent.EXTRA_TITLE, post.getTitle());
            Uri uri =Uri.parse(post.getFeaturedImage());
            sendIntent.setType("text/plain");
        return sendIntent;
    }

    public Intent getPlayStoreIntent(String packageName){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+packageName));
        return sendIntent;
    }

    public boolean isPackageInstalled(String packagename) {
        try {
            packageManager.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @SuppressLint("DefaultLocale")
    private  Intent getShareIntent(String type,Post post) {
        boolean found = false;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        type = type.toLowerCase();
        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = packageManager
                .queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type)
                        || info.activityInfo.name.toLowerCase().contains(type)) {
                    share.setPackage(info.activityInfo.packageName);
                    share.putExtra(Intent.EXTRA_SUBJECT, post.getTitle()+" "+shareSuffix);
                    share.putExtra(Intent.EXTRA_TEXT, post.getTitle()+ " - "+post.getShareUrl()+" "+shareSuffix);
                    found = true;
                    break;
                }
            }
            if (!found)
                return null;

            return share;
        }
        return null;
    }
}
