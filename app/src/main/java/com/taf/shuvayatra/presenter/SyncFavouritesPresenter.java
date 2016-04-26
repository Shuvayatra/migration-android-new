package com.taf.shuvayatra.presenter;

import com.taf.data.di.PerActivity;
import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Post;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.interfaces.MvpView;
import com.taf.shuvayatra.ui.interfaces.SplashScreenView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

@PerActivity
public class SyncFavouritesPresenter implements Presenter {

    private final UseCase mSyncUseCase;
    private final UseCase mFetchUseCase;

    SplashScreenView mView;

    @Inject
    public SyncFavouritesPresenter(@Named("favouritesSync") UseCase pSyncUseCase, @Named
            ("postList") UseCase pFetchUseCase) {
        mSyncUseCase = pSyncUseCase;
        mFetchUseCase = pFetchUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
        mSyncUseCase.unSubscribe();
    }

    @Override
    public void initialize(UseCaseData pData) {
        mView.showLoadingView();
        if (pData.getBoolean(UseCaseData.SEARCH_UN_SYNCED_DATA, false)) {
            searchUnSyncedData(pData);
        } else {
            syncLikes(pData);
        }
    }

    @Override
    public void attachView(MvpView pView) {
        mView = (SplashScreenView) pView;
    }

    private void searchUnSyncedData(UseCaseData pData) {
        this.mFetchUseCase.execute(new FetchUnSyncedSubscriber(), pData);
    }

    private void syncLikes(UseCaseData pData) {
        this.mSyncUseCase.execute(new SyncLikesSubscriber(), pData);
    }

    private final class FetchUnSyncedSubscriber extends DefaultSubscriber<List<Post>> {

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
        public void onNext(List<Post> pPosts) {
            super.onNext(pPosts);
            mView.unSyncedFavouritesSearched(pPosts);
            onCompleted();
        }
    }

    private final class SyncLikesSubscriber extends DefaultSubscriber<Boolean> {

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
        public void onNext(Boolean status) {
            super.onNext(status);
            mView.favouritesSynced(status);
            onCompleted();
        }
    }
}