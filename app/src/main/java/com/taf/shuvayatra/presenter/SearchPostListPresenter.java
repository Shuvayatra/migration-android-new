package com.taf.shuvayatra.presenter;

import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.PostResponse;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.views.MvpView;
import com.taf.shuvayatra.ui.views.SearchPostListView;

import javax.inject.Inject;
import javax.inject.Named;

public class SearchPostListPresenter implements Presenter {

    UseCase mUseCase;
    SearchPostListView mView;

    @Inject
    public SearchPostListPresenter(@Named("search-posts") UseCase useCase) {
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
        mUseCase.execute(new SearchPostsSubscriber(), pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (SearchPostListView) view;
    }

    public class SearchPostsSubscriber extends DefaultSubscriber<PostResponse> {

        @Override
        public void onCompleted() {
            mView.hideLoadingView();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            mView.hideLoadingView();
            mView.showErrorView(ErrorMessageFactory.create(mView.getContext(), new
                    DefaultErrorBundle((Exception) e).getException()));
        }

        @Override
        public void onNext(PostResponse pT) {
            mView.renderPosts(pT);
        }
    }
}
