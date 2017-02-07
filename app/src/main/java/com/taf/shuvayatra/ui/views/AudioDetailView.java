package com.taf.shuvayatra.ui.views;

import android.content.Context;

import java.io.File;

public interface AudioDetailView extends PostDetailView {

    void onAudioDownloadStarted(String pMessage);

    void onAudioFileNotFoundToShare();

    void showDeleteDialog(File file);

    Context getContext();
}
