package com.taf.shuvayatra.presenter;

import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.views.MvpView;
import com.taf.shuvayatra.ui.views.OnBoardingView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by umesh on 1/13/17.
 */

public class OnBoardingPresenter implements Presenter {

    OnBoardingView mView;
    UseCase mUsecase;

    @Inject
    public OnBoardingPresenter(@Named("send-user-info") UseCase useCase){
        mUsecase = useCase;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        mUsecase.unSubscribe();
    }

    @Override
    public void initialize(UseCaseData pData) {
        mUsecase.execute(new SendUserInfoSubscriber(), pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (OnBoardingView) view;
    }

    public class SendUserInfoSubscriber extends DefaultSubscriber<Boolean>{
        @Override
        public void onCompleted() {
            super.onCompleted();
            mView.hideLoadingView();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            e.printStackTrace();
            mView.hideLoadingView();
            mView.showErrorView(ErrorMessageFactory.create(mView.getContext(), new
                    DefaultErrorBundle((Exception) e).getException()));
        }

        @Override
        public void onNext(Boolean pT) {
            mView.onSendUserInfo();
        }
    }
}
