package com.taf.shuvayatra.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Telephony;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.taf.data.utils.Logger;
import com.taf.model.BaseModel;
import com.taf.model.Block;
import com.taf.model.Post;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static java.security.AccessController.getContext;

/**
 * Created by julian on 12/17/15.
 */
public class Utils {

    public static RecyclerView.ItemDecoration getBottomMarginDecoration(final Context context,
                                                                        @DimenRes final int padding) {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 0;
                if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
                    outRect.bottom = context.getResources().getDimensionPixelOffset(padding);
                }
            }
        };
    }


    public static RecyclerView.ItemDecoration getBottomMarginDecorationForGrid(final Context context,
                                                                               @DimenRes final int padding) {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 0;
                int position = parent.getChildAdapterPosition(view);
                int size = parent.getAdapter().getItemCount();

                // for second last item in adapter
                if (position == size - 2) {
                    GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
                    GridLayoutManager.SpanSizeLookup lookup = manager.getSpanSizeLookup();

                    int spanGroupIndex = lookup.getSpanGroupIndex(position, manager.getSpanCount());
                    if (lookup.getSpanGroupIndex(size - 1, manager.getSpanCount()) == spanGroupIndex) {
                        outRect.bottom = context.getResources().getDimensionPixelOffset(padding);
                    }
                }

                // for last item in adapter
                if (position == size - 1) {
                    outRect.bottom = context.getResources().getDimensionPixelOffset(padding);
                }
            }
        };
    }

    public static ColorStateList getIconColorTint(int color, int checkedColor) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        checkedColor,
                        color
                }
        );
    }

    public static void setLanguage(int i, Context context) {
        switch (i) {
            case MyConstants.Language.ENGLISH:
                Locale localeEN = new Locale("en");
                Locale.setDefault(localeEN);
                Configuration configEn = new Configuration();
                configEn.locale = localeEN;
                context.getApplicationContext().getResources().updateConfiguration(configEn, null);
                break;
            case MyConstants.Language.NEPALI:
                Locale localeNp = new Locale("np");
                Locale.setDefault(localeNp);
                Configuration configNp = new Configuration();
                configNp.locale = localeNp;
                context.getApplicationContext().getResources().updateConfiguration(configNp, null);
                break;
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static List<BaseModel> sortBlock(final List<BaseModel> modelList) {
        Collections.sort(modelList, new Comparator<BaseModel>() {
            @Override
            public int compare(BaseModel first, BaseModel second) {
                return (first.getPriority() < second.getPriority()) ? 1 :
                        ((first.getPriority() == second.getPriority()) ? 0 : -1);
            }
        });
        return modelList;
    }

    public static Intent create(PackageManager pm, Intent target, String title,
                                List<String> packageList, Post post) {
        Intent dummy = new Intent(target.getAction());
        dummy.setType(target.getType());
        List<ResolveInfo> resInfo = pm.queryIntentActivities(dummy, 0);

        List<HashMap<String, String>> metaInfo = new ArrayList<>();
        for (ResolveInfo ri : resInfo) {
            if (ri.activityInfo == null || !packageList.contains(ri.activityInfo.packageName))
                continue;

            HashMap<String, String> info = new HashMap<>();
            info.put("packageName", ri.activityInfo.packageName);
            info.put("className", ri.activityInfo.name);
            info.put("simpleName", String.valueOf(ri.activityInfo.loadLabel(pm)));
            metaInfo.add(info);
        }

        if (metaInfo.isEmpty()) {
            // Force empty chooser by setting a nonexistent target class.
            Intent emptyIntent = (Intent) target.clone();
            emptyIntent.setPackage("your.package.name");
            emptyIntent.setClassName("your.package.name", "NonExistingActivity");
            return Intent.createChooser(emptyIntent, title);
        }

        // Sort items by display name.
        Collections.sort(metaInfo, new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> map, HashMap<String, String> map2) {
                return map.get("simpleName").compareTo(map2.get("simpleName"));
            }
        });

        // create the custom intent list
        List<Intent> targetedIntents = new ArrayList<>();
        for (HashMap<String, String> mi : metaInfo) {
            Intent targetedShareIntent = (Intent) target.clone();
            targetedShareIntent.setPackage(mi.get("packageName"));
            targetedShareIntent.setClassName(mi.get("packageName"), mi.get("className"));
            targetedShareIntent.setType("text/plain");
            targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, post.getTitle());
            targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, post.getShareUrl()
                    .replace("://app", "://amp"));
            targetedIntents.add(targetedShareIntent);
        }

        Intent chooserIntent = Intent.createChooser(targetedIntents.get(0), title);
        targetedIntents.remove(0);
        Parcelable[] targetedIntentsParcelable =
                targetedIntents.toArray(new Parcelable[targetedIntents.size()]);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedIntentsParcelable);
        return chooserIntent;
    }

    private static final String TAG = "Utils";

    @Nullable
    public static String getDefaultSmsAppPackageName(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            return Telephony.Sms.getDefaultSmsPackage(context);
        else {
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .addCategory(Intent.CATEGORY_DEFAULT).setType("vnd.android-dir/mms-sms");
            final List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
            if (resolveInfos != null && !resolveInfos.isEmpty())
                return resolveInfos.get(0).activityInfo.packageName;
            return null;
        }
    }
}
