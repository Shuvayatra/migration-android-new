package com.taf.shuvayatra.presenter;

import android.util.Log;

import com.taf.data.utils.Logger;
import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Channel;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.views.ChannelView;
import com.taf.shuvayatra.ui.views.MvpView;
import com.taf.util.MyConstants;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by ngima on 11/3/16.
 */

public class ChannelListPresenter implements Presenter {

    ChannelView mView;
    UseCase mUseCase;

    private static final String TAG = "ChannelListPresenter";

    @Inject
    public ChannelListPresenter(@Named(MyConstants.UseCase.CASE_CHANNEL_LIST) UseCase mUseCase) {
        this.mUseCase = mUseCase;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        mUseCase.unSubscribe();
    }

    @Override
    public void initialize(UseCaseData pData) {
        mView.showLoadingView();
        mUseCase.execute(new ChannelSubscriber(), pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (ChannelView) view;
    }

    class ChannelSubscriber extends DefaultSubscriber<List<Channel>>{
        @Override
        public void onCompleted() {
            mView.hideLoadingView();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);

            Log.e(TAG, ">>> presenter onError: called <<<", e);
            mView.hideLoadingView();
            mView.showErrorView(ErrorMessageFactory.create(mView.getContext(), new
                    DefaultErrorBundle((Exception) e).getException()));
        }

        @Override
        public void onNext(List<Channel> pT) {
            Logger.e(TAG, ">>> call to on next <<<");
            mView.renderChannel(pT);
            onCompleted();
        }
    }
}
