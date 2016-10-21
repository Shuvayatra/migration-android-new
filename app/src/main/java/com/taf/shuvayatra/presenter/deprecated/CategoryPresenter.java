package com.taf.shuvayatra.presenter.deprecated;

import com.taf.data.utils.Logger;
import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Category;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.presenter.Presenter;
import com.taf.shuvayatra.ui.deprecated.interfaces.CategoryView;
import com.taf.shuvayatra.ui.views.MvpView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Nirazan-PC on 4/21/2016.
 */
public class CategoryPresenter implements Presenter {

    CategoryView mView;
    UseCase mUseCase;

    @Inject
    public CategoryPresenter(@Named("sectionCategory") UseCase pUseCase) {
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
        mView.showLoadingView();
        getJourneyCategories(pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (CategoryView) view;
    }

    private void getJourneyCategories(UseCaseData pData) {
        mUseCase.execute(new JourneySubscriber(), pData);
    }

    private final class JourneySubscriber extends DefaultSubscriber<List<Category>> {

        @Override
        public void onCompleted() {
            mView.hideLoadingView();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Logger.e("JourneySubscriber_onError", e.getLocalizedMessage());
            mView.hideLoadingView();
            mView.showErrorView(ErrorMessageFactory.create(mView.getContext(), new
                    DefaultErrorBundle((Exception) e).getException()));
        }

        @Override
        public void onNext(List<Category> pCategories) {
            mView.renderCategories(pCategories);
            onCompleted();
        }
    }
}
