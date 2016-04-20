package com.taf.shuvayatra.ui.interfaces;

import android.content.Context;

public interface AudioDetailView extends MvpView {
    void onMediaPlayPause();

    void onSeekTo(int progress);

    void onBufferStarted();

    void onBufferStopped();

    void onMediaPrepared();

    void onMediaComplete();

    void playStatusChanged(boolean isPlaying);

    Context getContext();
}
