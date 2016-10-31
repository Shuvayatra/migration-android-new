package com.taf.shuvayatra.presenter;

import com.taf.data.di.PerActivity;
import com.taf.data.utils.Logger;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.shuvayatra.ui.views.MvpView;

import javax.inject.Inject;
import javax.inject.Named;

@PerActivity
public class UserActionsSyncPresenter implements Presenter {

    private final UseCase mUseCase;

    @Inject
    public UserActionsSyncPresenter(@Named("actions_sync") UseCase useCase) {
        mUseCase = useCase;
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
        Logger.d("UserActionsSyncPresenter_initialize", "test: sync init");
        mUseCase.execute(new SyncLikesSubscriber(), pData);
    }

    @Override
    public void attachView(MvpView pView) {

    }

    private final class SyncLikesSubscriber extends DefaultSubscriber<Boolean> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(Boolean status) {
            super.onNext(status);
        }
    }
}