package com.taf.shuvayatra.presenter;

import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Post;
import com.taf.shuvayatra.ui.views.MvpView;
import com.taf.shuvayatra.ui.views.PostListView;
import com.taf.shuvayatra.ui.views.UserAccountView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by umesh on 1/11/17.
 */

public class UserAccountPresenter implements Presenter {

    UseCase mUseCase;
    UserAccountView mView;

    @Inject
    public UserAccountPresenter(@Named("favourite-posts") UseCase useCase) {
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
        mUseCase.execute(new FavouritePostSubscriber(), pData);

    }

    @Override
    public void attachView(MvpView view) {
        mView = (UserAccountView) view;
    }

    private class FavouritePostSubscriber extends DefaultSubscriber<List<Post>> {

        @Override
        public void onCompleted() {
            super.onCompleted();
            mView.hideLoadingView();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);

        }

        @Override
        public void onNext(List<Post> pT) {
            mView.renderPosts(pT);
        }
    }
}
