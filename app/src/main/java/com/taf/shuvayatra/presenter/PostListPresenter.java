package com.taf.shuvayatra.presenter;

import com.taf.data.utils.Logger;
import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Post;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.interfaces.MvpView;
import com.taf.shuvayatra.ui.interfaces.PostListView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class PostListPresenter implements Presenter {

    UseCase mUseCase;
    PostListView mView;

    @Inject
    public PostListPresenter(@Named("postList") UseCase pUseCase) {
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
        loadPostList(pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (PostListView) view;
    }

    private void loadPostList(UseCaseData pData) {
        mView.showLoadingView();
        this.mUseCase.execute(new PostSubscriber(), pData);
    }

    private final class PostSubscriber extends DefaultSubscriber<List<Post>> {

        @Override
        public void onCompleted() {
            mView.hideLoadingView();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
//            Logger.e("PostSubscriber", "error message: "+e.getMessage());
            mView.hideLoadingView();
            mView.showErrorView(ErrorMessageFactory.create(mView.getContext(), new
                    DefaultErrorBundle((Exception) e).getException()));
        }

        @Override
        public void onNext(List<Post> pPosts) {
            mView.renderPostList(pPosts, pPosts.isEmpty() ? 0 : pPosts.get(0).getCurrentOffset(),
                    pPosts.isEmpty() ? 0 : pPosts.get(0).getTotalCount());
            onCompleted();
        }
    }

}
