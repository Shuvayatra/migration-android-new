package com.taf.shuvayatra.presenter;

import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.ScreenModel;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.views.HomeActivityView;
import com.taf.shuvayatra.ui.views.MvpView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by umesh on 1/14/17.
 */

public class HomeActivityPresenter implements Presenter {

    UseCase mUseCase;
    HomeActivityView mView;

    @Inject
    public HomeActivityPresenter(@Named("screens")UseCase useCase){
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
        mUseCase.execute(new ScreenSubscriber(),pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (HomeActivityView) view;
    }

    private class ScreenSubscriber extends DefaultSubscriber<List<ScreenModel>>{

        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            mView.showErrorView(ErrorMessageFactory.create(mView.getContext(), new
                    DefaultErrorBundle((Exception) e).getException()));
        }

        @Override
        public void onNext(List<ScreenModel> pT) {
            mView.renderScreens(pT);
        }
    }
}
