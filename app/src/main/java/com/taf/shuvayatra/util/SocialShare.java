package com.taf.shuvayatra.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

/**
 * Created by asim on 8/17/16.
 */
public class SocialShare {

    private PackageManager packageManager;

    public SocialShare(Context context){
        packageManager = context.getPackageManager();
    }

    public  Intent getFacebookIntent(String url) {
        Intent intent = getShareIntent("katana");
        if(url!=null){
            Uri uri =Uri.parse(url);
            intent.putExtra(Intent.EXTRA_TEXT,url);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        intent.setType("text/plain");
        if (intent != null) {
            return intent;
        }
        return null;
    }

    public  Intent getTwitterIntent(String url) {
        Intent intent = getShareIntent("twitter");
        if(url!=null){
            Uri uri =Uri.parse(url);
            intent.putExtra(Intent.EXTRA_TEXT,url);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        intent.setType("text/plain");
        if (intent != null) {
            return intent;
        }
        return null;
    }

    public  Intent getViberIntent(String url) {
        Intent intent = getShareIntent("viber");
        if(url!=null){
            Uri uri =Uri.parse(url);
            intent.putExtra(Intent.EXTRA_TEXT,url);
        }
        intent.setType("text/plain");
        if (intent != null) {
            return intent;
        }
        return null;
    }

    public  Intent getSmsIntent(String url) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        sendIntent.putExtra("sms_body", url);
        sendIntent.putExtra(Intent.EXTRA_TEXT, url);
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));
        return sendIntent;
    }

    public  Intent getWhatsApp(String url) {
        Intent intent = getShareIntent("whatsapp");
        if(url!=null){
            Uri uri =Uri.parse(url);
            intent.putExtra(Intent.EXTRA_TEXT,url);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        intent.setType("text/plain");
        if (intent != null) {
            return intent;
        }
        return null;
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
    private  Intent getShareIntent(String type) {
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
