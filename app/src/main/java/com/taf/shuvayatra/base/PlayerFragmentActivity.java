package com.taf.shuvayatra.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.taf.data.utils.Logger;
import com.taf.shuvayatra.MyApplication;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.ui.fragment.MiniPlayerFragment;
import com.taf.shuvayatra.ui.interfaces.PlayerFragmentCallback;

import butterknife.BindView;

public abstract class PlayerFragmentActivity extends BaseActivity implements
        PlayerFragmentCallback, MiniPlayerFragment.ContainerClick {

    @BindView(R.id.content_player)
    View mPlayer;

    @BindView(R.id.bottom_sheet)
    NestedScrollView bottomSheet;

    private BottomSheetBehavior bottomSheetBehavior;

    public boolean alwaysShowPlayer() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        togglePlayerFragment();
    }


    public void togglePlayerFragment() {
        Fragment playerFragment = getSupportFragmentManager().findFragmentByTag
                (MiniPlayerFragment.TAG);
        if (showMiniPlayer()) {
            if (playerFragment == null) playerFragment = MiniPlayerFragment.newInstance(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_player, playerFragment, MiniPlayerFragment.TAG)
                    .commit();
            initBottomSheet();
        } else {
            if (playerFragment != null) {
                getSupportFragmentManager().beginTransaction().remove(playerFragment).commit();
            }
        }
    }

    public void initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
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
                ((MyApplication) getApplicationContext()).mService.isMediaValid();
    }


    @Override
    public void removePlayer() {
        ((MyApplication) getApplicationContext()).mService.stopPlayback();
        Fragment playerFragment = getSupportFragmentManager().findFragmentByTag
                (MiniPlayerFragment.TAG);
        if (playerFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(playerFragment)
                    .commit();
        }
    }
}
