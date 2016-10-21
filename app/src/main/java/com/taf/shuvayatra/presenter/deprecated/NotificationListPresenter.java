package com.taf.shuvayatra.presenter.deprecated;

import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Notification;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.presenter.Presenter;
import com.taf.shuvayatra.ui.views.MvpView;
import com.taf.shuvayatra.ui.deprecated.interfaces.NotificationView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class NotificationListPresenter implements Presenter {
    UseCase mUseCase;
    NotificationView mView;

    @Inject
    public NotificationListPresenter(@Named("notification") UseCase pUseCase) {
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
        mView.showLoadingView();
        mUseCase.execute(new NotificationListSubscriber(), pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (NotificationView) view;
    }

    public class NotificationListSubscriber extends DefaultSubscriber<List<Notification>> {
        @Override
        public void onCompleted() {
            mView.hideLoadingView();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            mView.hideLoadingView();
            mView.showErrorView(ErrorMessageFactory.create(mView.getContext(), new
                    DefaultErrorBundle((Exception) e).getException()));
        }

        @Override
        public void onNext(List<Notification> pNotifications) {
            mView.renderNotifications(pNotifications);
            onCompleted();
        }
    }
}
