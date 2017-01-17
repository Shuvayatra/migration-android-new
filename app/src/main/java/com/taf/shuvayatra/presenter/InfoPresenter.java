package com.taf.shuvayatra.presenter;

import android.util.Log;

import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Info;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.views.InfoView;
import com.taf.shuvayatra.ui.views.MvpView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by yipl on 1/16/17.
 */

public class InfoPresenter implements Presenter {
    private static final String TAG = "InfoPresenter";

    final UseCase mUseCase;
    InfoView mView;

    @Inject
    public InfoPresenter(@Named("info") UseCase useCase) {
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
        mUseCase.execute(new InfoSubscriber(), pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (InfoView) view;
    }

    private final class InfoSubscriber extends DefaultSubscriber<Info> {
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
        public void onNext(Info info) {
            mView.render(info);
        }
    }
}
