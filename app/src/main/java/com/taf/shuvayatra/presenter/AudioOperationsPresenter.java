package com.taf.shuvayatra.presenter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.taf.data.utils.AppPreferences;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Post;
import com.taf.shuvayatra.R;
import com.taf.shuvayatra.ui.views.AudioDetailView;
import com.taf.shuvayatra.ui.views.MvpView;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

public class AudioOperationsPresenter implements Presenter {

    UseCase mUseCase;
    AudioDetailView mView;
    AppPreferences mPreferences;
    String folderName;

    DownloadManager mDownloadManager;

    @Inject
    public AudioOperationsPresenter(@Named("download_start") UseCase pUseCase, AppPreferences
            pPreferences) {
        mUseCase = pUseCase;
        mPreferences = pPreferences;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
    }

    @Override
    public void initialize(UseCaseData pData) {
        mDownloadManager = (DownloadManager) mView.getContext().getSystemService(Context
                .DOWNLOAD_SERVICE);
        folderName = mView.getContext().getString(R.string.app_name);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (AudioDetailView) view;
    }

    public void downloadAudioPost(Post pAudio) throws IOException {
        String mediaUrl = pAudio.getData().getMediaUrl().replace(" ", "%20");
        String fileName = mediaUrl.substring(mediaUrl.lastIndexOf("/") + 1).replace("%20", " ");
        File audioFile = getAudioFile(fileName);

        if (!audioFile.exists()) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mediaUrl));
            request.setNotificationVisibility(DownloadManager.Request
                    .VISIBILITY_VISIBLE);
            request.setTitle(pAudio.getTitle());
            request.setDescription(mView.getContext().getResources().getString(R.string
                    .message_downloading));

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, folderName +
                    "/" + fileName);
            request.setVisibleInDownloadsUi(true);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            long reference = mDownloadManager.enqueue(request);
            pAudio.setDownloadReference(reference);
            mPreferences.addDownloadReference(reference);

            /*UseCaseData data = new UseCaseData();
            data.putLong(UseCaseData.DOWNLOAD_REFERENCE, reference);
            mUseCase.execute(new DefaultSubscriber(), data);*/
        } else {
            mView.onAudioDownloadStarted(mView.getContext().getString(R.string
                    .message_already_downloaded, pAudio.getTitle()));
        }
    }

    public void shareViaBluetooth(Post pAudio) {
        String mediaUrl = pAudio.getData().getMediaUrl().replace(" ", "%20");
        String fileName = mediaUrl.substring(mediaUrl.lastIndexOf("/") + 1).replace("%20", " ");
        File audioFile = getAudioFile(fileName);

        if (audioFile.exists()) {
            String path = audioFile.getAbsolutePath();
            if (!(path.startsWith("file") || path.startsWith("content") || path.startsWith("FILE") || path
                    .startsWith("CONTENT"))) {
                path = "file://" + path;
            }
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            Uri audioUri = Uri.parse(path);

            sharingIntent.setType("audio/mpeg");
            sharingIntent.setPackage("com.android.bluetooth");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, audioUri);
            mView.getContext().startActivity(Intent.createChooser(sharingIntent, mView.getContext
                    ().getString(R.string.share_audio)));
        } else {
            mView.onAudioFileNotFoundToShare();
        }
    }

    private File getAudioFile(String fileName) {
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File dir = new File(root, folderName);
        dir.mkdirs();
        return new File(dir, fileName);
    }
}
