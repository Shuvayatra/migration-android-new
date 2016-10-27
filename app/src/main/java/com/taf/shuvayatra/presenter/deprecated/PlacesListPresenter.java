package com.taf.shuvayatra.presenter.deprecated;

import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Post;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.presenter.Presenter;
import com.taf.shuvayatra.ui.views.MvpView;
import com.taf.shuvayatra.ui.deprecated.interfaces.PlacesListView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class PlacesListPresenter implements Presenter {

    UseCase mUseCase;
    PlacesListView mView;

    @Inject
    public PlacesListPresenter(@Named("postList") UseCase pUseCase) {
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
        loadPlacesList(pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (PlacesListView) view;
    }

    private void loadPlacesList(UseCaseData pData) {
        mView.showLoadingView();
        this.mUseCase.execute(new PlacesSubscriber(), pData);
    }

    private final class PlacesSubscriber extends DefaultSubscriber<List<Post>> {

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
        public void onNext(List<Post> pPlaces) {
            mView.renderPlaces(pPlaces);
            onCompleted();
        }
    }

}
