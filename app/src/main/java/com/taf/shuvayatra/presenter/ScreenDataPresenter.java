package com.taf.shuvayatra.presenter;

import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.ScreenDataModel;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.views.MvpView;
import com.taf.shuvayatra.ui.views.ScreenDataView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by umesh on 1/13/17.
 */

public class ScreenDataPresenter implements Presenter {

    UseCase mUseCase;
    ScreenDataView mView;

    @Inject
    public ScreenDataPresenter(@Named("screen-data") UseCase useCase) {
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
        mView.showLoadingView();
        mUseCase.execute(new ScreenDataSubscriber(), pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (ScreenDataView) view;
    }

    private class ScreenDataSubscriber extends DefaultSubscriber<ScreenDataModel> {
        @Override
        public void onCompleted() {
            super.onCompleted();
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
        public void onNext(ScreenDataModel pT) {
            mView.renderScreenData(pT);
        }
    }
}
