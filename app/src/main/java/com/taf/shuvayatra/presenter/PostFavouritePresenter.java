package com.taf.shuvayatra.presenter;

import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.views.MvpView;
import com.taf.shuvayatra.ui.views.PostDetailView;

import javax.inject.Inject;
import javax.inject.Named;

public class PostFavouritePresenter implements Presenter {

    UseCase mUseCase;
    PostDetailView mView;

    private boolean favState = false;

    @Inject
    public PostFavouritePresenter(@Named("post_favourite") UseCase pUseCase) {
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
        favState = pData.getBoolean(UseCaseData.FAVOURITE_STATE, false);
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
            mView.onPostFavouriteStateUpdated(favState);
            mView.showErrorView(ErrorMessageFactory.create(mView.getContext(), new
                    DefaultErrorBundle((Exception) e).getException()));
        }

        @Override
        public void onNext(Boolean pStatus) {
            mView.onPostFavouriteStateUpdated(pStatus);
        }
    }
}
