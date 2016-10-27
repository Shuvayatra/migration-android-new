package com.taf.shuvayatra.presenter.deprecated;

import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Post;
import com.taf.shuvayatra.presenter.Presenter;
import com.taf.shuvayatra.ui.views.MvpView;
import com.taf.shuvayatra.ui.deprecated.interfaces.PostView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Nirazan-PC on 5/6/2016.
 */
public class SinglePostPresenter implements Presenter {

    private final UseCase mUseCase;
    PostView mPostView;

    @Inject
    public SinglePostPresenter(@Named("single_post")UseCase pUseCase){
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
        mUseCase.execute(new SinglePostSubscriber(),pData);
    }

    @Override
    public void attachView(MvpView view) {
        mPostView = (PostView) view;
    }

    public class SinglePostSubscriber extends DefaultSubscriber<Post>{
        @Override
        public void onCompleted() {
            mPostView.hideLoadingView();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(Post pPost) {
            onCompleted();
            mPostView.postLoadCompleted(pPost);
        }
    }
}
