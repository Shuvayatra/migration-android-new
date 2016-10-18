package com.taf.shuvayatra.presenter.deprecated;

import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.shuvayatra.presenter.Presenter;
import com.taf.shuvayatra.ui.deprecated.interfaces.MvpView;
import com.taf.shuvayatra.ui.deprecated.interfaces.PostDetailView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Nirazan-PC on 5/4/2016.
 */
public class PostViewCountPresenter implements Presenter {

    UseCase mUseCase;
    PostDetailView mView;

    @Inject
    public PostViewCountPresenter(@Named("view_count_update") UseCase pUseCase){
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

    }

    @Override
    public void initialize(UseCaseData pData) {
        mUseCase.execute( new ViewCountUpdateSubscriber(),pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (PostDetailView) view;
    }

    public class ViewCountUpdateSubscriber extends DefaultSubscriber<Long>{
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(Long pId) {
            mView.onViewCountUpdated();
        }
    }
}
