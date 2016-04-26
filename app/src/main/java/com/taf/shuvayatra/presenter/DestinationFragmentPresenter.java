package com.taf.shuvayatra.presenter;

import com.taf.data.utils.Logger;
import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Category;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.interfaces.DestinationView;
import com.taf.shuvayatra.ui.interfaces.MvpView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Nirazan-PC on 4/25/2016.
 */
public class DestinationFragmentPresenter implements Presenter {

    DestinationView mView;
    UseCase mUseCase;

    @Inject
    public DestinationFragmentPresenter(@Named("sectionCategory") UseCase pUseCase){
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
        mUseCase.execute(new DestinationSubscriber(),pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (DestinationView) view;
    }

    private final class DestinationSubscriber extends DefaultSubscriber<List<Category>> {

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
        public void onNext(List<Category> pCountries) {
            Logger.e("DestinationSubscriber", "coutnreis: "+ pCountries);
            if(pCountries!=null) {
                mView.renderCountries(pCountries);
            }
            onCompleted();
        }
    }
}
