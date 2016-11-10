package com.taf.shuvayatra.presenter;

import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.PodcastResponse;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.views.MvpView;
import com.taf.shuvayatra.ui.views.PodcastListView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by julian on 10/18/16.
 */

public class PodcastListPresenter implements Presenter {

    final UseCase mUseCase;
    PodcastListView mView;

    @Inject
    public PodcastListPresenter(@Named("podcast_list") UseCase useCase) {
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
        mUseCase.execute(new PodcastListSubscriber(), pData);
    }

    @Override
    public void attachView(MvpView view) {
        this.mView = (PodcastListView) view;
    }

    private final class PodcastListSubscriber extends DefaultSubscriber<PodcastResponse> {
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
        public void onNext(PodcastResponse response) {
            if (response.getData() != null) {
                mView.renderPodcasts(response.getData().getData());
            }
            onCompleted();
        }
    }
}
