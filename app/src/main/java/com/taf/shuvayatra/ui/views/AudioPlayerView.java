package com.taf.shuvayatra.ui.views;

/**
 * Created by julian on 10/24/16.
 */

public interface AudioPlayerView {
    void onBufferStarted();

    void onBufferStopped();

    void onMediaPrepared();

    void onMediaComplete();

    void playStatusChanged(boolean isPlaying);

    void onMediaProgressReset();

    void onMediaProgressChanged(long[] lengths);
}
