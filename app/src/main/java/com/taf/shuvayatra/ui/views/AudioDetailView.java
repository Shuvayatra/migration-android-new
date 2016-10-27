package com.taf.shuvayatra.ui.views;

import android.content.Context;

public interface AudioDetailView extends PostDetailView {

    void onAudioDownloadStarted(String pMessage);

    void onAudioFileNotFoundToShare();

    Context getContext();
}
