package com.taf.shuvayatra.presenter;

import com.taf.data.utils.Logger;
import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Post;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.views.MvpView;
import com.taf.shuvayatra.ui.views.PostDetailView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by julian on 10/18/16.
 */

public class PostDetailPresenter implements Presenter {

    final UseCase mUseCase;
    PostDetailView mView;

    @Inject
    public PostDetailPresenter(@Named("post") UseCase useCase) {
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
        Logger.d("PostDetailPresenter_initialize", "mview: " + mView);
        mView.showLoadingView();
        mUseCase.execute(new PostDetailSubscriber(), pData);
    }

    @Override
    public void attachView(MvpView view) {
        this.mView = (PostDetailView) view;
        Logger.d("PostDetailPresenter_attachView", "mview: " + mView);
    }

    private final class PostDetailSubscriber extends DefaultSubscriber<Post> {
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
        public void onNext(Post post) {
            mView.renderPost(post);
        }
    }
}
