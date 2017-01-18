package com.taf.shuvayatra.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;

import com.taf.data.utils.Logger;
import com.taf.model.Podcast;
import com.taf.shuvayatra.MyApplication;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.media.MediaService;
import com.taf.shuvayatra.ui.fragment.MiniPlayerFragment;
import com.taf.shuvayatra.ui.interfaces.PlayerFragmentCallback;

import java.util.List;

import butterknife.BindView;

public abstract class PlayerFragmentActivity extends BaseActivity implements
        PlayerFragmentCallback, MiniPlayerFragment.ContainerClick {

    @BindView(R.id.content_player)
    View mPlayer;

    private BottomSheetBehavior bottomSheetBehavior;

    private boolean mediaPlayerVisible = false;

    public boolean alwaysShowPlayer() {
        return false;
    }

    private MiniPlayerFragment playerFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");

    }

    @Override
    protected void onResume() {
        super.onResume();
        togglePlayerFragment();
    }

    public void togglePlayerFragment() {
        Logger.e(TAG + "_MethodCall", ">>> toggleFragment()");

        Logger.e(TAG, ">>> service is null? " + String.valueOf(null == ((MyApplication)
                getApplicationContext()).mService));

        Logger.e(TAG, ">>> service is media valid: " + String.valueOf(((MyApplication)
                getApplicationContext()).mService != null &&
                ((MyApplication) getApplicationContext()).mService.isMediaValid()));

        Logger.e(TAG, ">>> service is media playing: " + String.valueOf(((MyApplication)
                getApplicationContext()).mService != null &&
                ((MyApplication) getApplicationContext()).mService.isMediaPlaying()));

        Logger.e(TAG, ">>> service current play type: " + String.valueOf(((MyApplication)
                getApplicationContext()).mService != null ?
                ((MyApplication) getApplicationContext()).mService.getCurrentPlayType() : false));

        playerFragment = (MiniPlayerFragment) getSupportFragmentManager().findFragmentByTag
                (MiniPlayerFragment.TAG);
        if (showMiniPlayer()) {
            Logger.e(TAG + "_MethodCall", ">>> media player is visible");
            if (playerFragment == null) playerFragment = MiniPlayerFragment.newInstance(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_player, playerFragment, MiniPlayerFragment.TAG)
                    .commit();
            mediaPlayerVisible = true;
            try {
                if (mPlayer != null)
                    initBottomSheet();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Logger.e(TAG + "_MethodCall", ">>> media player is gone");
            if (playerFragment != null) {
                getSupportFragmentManager().beginTransaction().remove(playerFragment).commit();
                mediaPlayerVisible = false;
            }
        }
    }

    public MiniPlayerFragment getPlayerFragment() {
        return playerFragment;
    }

    public MediaService getMediaService() {
        return ((MyApplication) getApplicationContext()).mService;
    }

    public List<Podcast> getServicePlaylist() {
        return ((MyApplication) getApplicationContext()).mService == null ? null :
                ((MyApplication) getApplicationContext()).mService.getPodcasts();
    }

    public boolean isMediaPlayerVisible() {
        Logger.e(TAG, ">>> media player visibility: " + mediaPlayerVisible);
        return mediaPlayerVisible;
    }

    public void initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(mPlayer);
        bottomSheetBehavior.setPeekHeight(getResources().getDimensionPixelOffset(R.dimen.mini_media_player_peek_height));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior
                .BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private static final String TAG = "PlayerFragmentActivity";

    @Override
    public void onContainerClick() {
        Logger.e(TAG, ">>> callback call");
        Logger.e(TAG, "sheet state: " + bottomSheetBehavior.getState());
        bottomSheetBehavior.setState(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED ?
                BottomSheetBehavior.STATE_COLLAPSED : BottomSheetBehavior.STATE_EXPANDED);
        Logger.e(TAG, "sheet state: " + bottomSheetBehavior.getState());
    }

    public boolean showMiniPlayer() {
        return alwaysShowPlayer() || ((MyApplication) getApplicationContext()).mService != null &&
                ((MyApplication) getApplicationContext()).mService.getCurrentPlayType()
                        != MediaService.PlayType.NONE;
    }

    @Override
    public void removePlayer() {
        ((MyApplication) getApplicationContext()).mService.stopPlayback(false);
        if (playerFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(playerFragment)
                    .commit();
            mediaPlayerVisible = false;
        }
    }

    public void hidePlayer() {
        if (playerFragment != null) {
            Logger.e(TAG + "_MethodCall", "hiding player()");
            getSupportFragmentManager().beginTransaction()
                    .remove(playerFragment)
                    .commit();
            mediaPlayerVisible = false;
        }
    }
}
