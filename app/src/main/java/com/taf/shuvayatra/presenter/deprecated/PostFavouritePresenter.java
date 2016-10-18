package com.taf.shuvayatra.presenter.deprecated;

import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.presenter.Presenter;
import com.taf.shuvayatra.ui.deprecated.interfaces.MvpView;
import com.taf.shuvayatra.ui.deprecated.interfaces.PostDetailView;

import javax.inject.Inject;
import javax.inject.Named;

public class PostFavouritePresenter implements Presenter {

    UseCase mUseCase;
    PostDetailView mView;

    @Inject
    public PostFavouritePresenter(@Named("favouriteUpdate") UseCase pUseCase) {
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
        mUseCase.execute(new PostFavouriteSubscriber(), pData);
    }

    @Override
    public void attachView(MvpView view) {
        this.mView = (PostDetailView) view;
    }

    private final class PostFavouriteSubscriber extends DefaultSubscriber<Boolean> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            mView.showErrorView(ErrorMessageFactory.create(mView.getContext(), new
                    DefaultErrorBundle((Exception) e).getException()));
        }

        @Override
        public void onNext(Boolean pStatus) {
            mView.onPostFavouriteStateUpdated(pStatus);
            onCompleted();
        }
    }
}
