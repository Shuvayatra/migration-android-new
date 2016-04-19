package com.taf.shuvayatra.presenter;

import com.taf.data.di.PerActivity;
import com.taf.data.utils.Logger;
import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.LatestContent;
import com.taf.shuvayatra.base.BaseActivity;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.interfaces.MvpView;
import com.taf.shuvayatra.ui.interfaces.SplashScreenView;
import com.taf.shuvayatra.util.AppPreferences;

import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Named;

@PerActivity
public class LatestContentPresenter implements Presenter {

    private final UseCase mUseCase;
    private final AppPreferences mPref;
    SplashScreenView mView;

    @Inject
    public LatestContentPresenter(@Named("latest") UseCase pUseCase, AppPreferences pref) {
        mUseCase = pUseCase;
        mPref = pref;
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
        getLatestContent(pData);
    }

    @Override
    public void attachView(MvpView pView) {
        mView = (SplashScreenView) pView;
    }

    private void getLatestContent(UseCaseData pData) {
        this.mUseCase.execute(new LatestContentSubscriber(), pData);
    }

    private final class LatestContentSubscriber extends DefaultSubscriber<LatestContent> {

        @Override
        public void onCompleted() {
            mView.hideLoadingView();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Logger.e("LatestContentSubscriber_onError", e.getLocalizedMessage());
            mView.hideLoadingView();
            mView.showErrorView(ErrorMessageFactory.create(mView.getContext(), new
                    DefaultErrorBundle((Exception) e).getException()));
        }

        @Override
        public void onNext(LatestContent pLatestContent) {
            if (pLatestContent != null) {
                long timestamp = Calendar.getInstance().getTimeInMillis();
                ((BaseActivity) mView).getPreferences().setLastUpdateStamp((timestamp / 1000L));
            }
            mView.latestContentFetched();
            onCompleted();
        }
    }
}
