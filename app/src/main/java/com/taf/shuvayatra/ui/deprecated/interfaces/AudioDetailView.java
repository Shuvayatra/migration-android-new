package com.taf.shuvayatra.ui.deprecated.interfaces;

import android.content.Context;

public interface AudioDetailView extends PostDetailView {
    void onMediaPlayPause();

    void onSeekTo(int progress);

    void onBufferStarted();

    void onBufferStopped();

    void onMediaPrepared();

    void onMediaComplete();

    void playStatusChanged(boolean isPlaying);

    void onAudioDownloadStarted(String pMessage);

    void onAudioFileNotFoundToShare();

    Context getContext();
}
