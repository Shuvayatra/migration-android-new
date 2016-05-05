package com.taf.shuvayatra.presenter;

import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.shuvayatra.ui.interfaces.MvpView;
import com.taf.shuvayatra.ui.interfaces.PostDetailView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Nirazan-PC on 5/5/2016.
 */
public class PostShareCountPresenter implements Presenter {

    UseCase mUseCase;
    PostDetailView mPostDetailView;

    @Inject
    public PostShareCountPresenter(@Named("share_count_update") UseCase pUseCase){
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
        mUseCase.execute(new PostShareCountSubscriber(),pData);
    }

    @Override
    public void attachView(MvpView view) {
        mPostDetailView = (PostDetailView) view;
    }

    public class PostShareCountSubscriber extends DefaultSubscriber<Long>{
        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(Long pLong) {
            mPostDetailView.onShareCountUpdate();
        }
    }
}
