package com.taf.shuvayatra.presenter;

import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Block;
import com.taf.model.Post;
import com.taf.model.PostResponse;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.views.MvpView;
import com.taf.shuvayatra.ui.views.NewsView;
import com.taf.shuvayatra.ui.views.PostListView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by yipl on 1/11/17.
 */

public class NewsPresenter implements Presenter {

    private static final String TAG = "NewsPresenter";

    final UseCase mUseCase;
    NewsView mView;

    @Inject
    public NewsPresenter(@Named("news") UseCase mUseCase) {
        this.mUseCase = mUseCase;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void initialize(UseCaseData pData) {
        mUseCase.execute(new NewsSubscriber(), pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (NewsView) view;
    }

    private final class NewsSubscriber extends DefaultSubscriber<PostResponse>{
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
        public void onNext(PostResponse blocks) {
            mView.renderBlocks(blocks);
        }
    }
}
