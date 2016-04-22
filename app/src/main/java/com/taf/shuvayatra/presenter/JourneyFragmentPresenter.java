package com.taf.shuvayatra.presenter;

import com.taf.data.utils.Logger;
import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Category;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.interfaces.JourneyView;
import com.taf.shuvayatra.ui.interfaces.MvpView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Nirazan-PC on 4/21/2016.
 */
public class JourneyFragmentPresenter implements Presenter {

    JourneyView mView;
    UseCase mUseCase;

    @Inject
    public JourneyFragmentPresenter(@Named("sectionCategory") UseCase pUseCase){
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

    private void getJourneyCategories(UseCaseData pData) {
        mUseCase.execute(new JourneySubscriber(),pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (JourneyView) view;
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
