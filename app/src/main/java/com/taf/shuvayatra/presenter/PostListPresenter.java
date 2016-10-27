package com.taf.shuvayatra.presenter;

import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.PostResponse;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.views.MvpView;
import com.taf.shuvayatra.ui.views.PostListView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by julian on 10/18/16.
 */

public class PostListPresenter implements Presenter {

    final UseCase mUseCase;
    PostListView mView;

    @Inject
    public PostListPresenter(@Named("post_list") UseCase useCase) {
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
        mUseCase.execute(new PostListSubscriber(), pData);
    }

    @Override
    public void attachView(MvpView view) {
        this.mView = (PostListView) view;
    }

    private final class PostListSubscriber extends DefaultSubscriber<PostResponse> {
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
        public void onNext(PostResponse response) {
            mView.renderPostList(response);
        }
    }
}
