package com.taf.shuvayatra.presenter;

import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.shuvayatra.ui.interfaces.MvpView;

import javax.inject.Inject;
import javax.inject.Named;

public class DownloadCompletePresenter implements Presenter {

    UseCase mUseCase;
    MvpView mView;

    @Inject
    public DownloadCompletePresenter(@Named("download_complete") UseCase pUseCase) {
        mUseCase = pUseCase;
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
        mUseCase.execute(new DefaultSubscriber(), pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = view;
    }
}
