package com.taf.shuvayatra.base;

import android.support.v4.app.Fragment;
import android.view.View;

import com.taf.shuvayatra.MyApplication;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.ui.fragment.MiniPlayerFragment;
import com.taf.shuvayatra.ui.interfaces.PlayerFragmentCallback;

import butterknife.BindView;

public abstract class PlayerFragmentActivity extends BaseActivity implements
        PlayerFragmentCallback {

    @BindView(R.id.content_player)
    View mPlayer;

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
        if (alwaysShowPlayer() || ((MyApplication) getApplicationContext()).mService != null &&
                ((MyApplication) getApplicationContext()).mService.isMediaValid()) {
            if (playerFragment == null) playerFragment = new MiniPlayerFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_player, playerFragment, MiniPlayerFragment.TAG)
                    .commit();
        } else {
            if (playerFragment != null) {
                getSupportFragmentManager().beginTransaction().remove(playerFragment).commit();
            }
        }
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
