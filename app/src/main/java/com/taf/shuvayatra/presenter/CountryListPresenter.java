package com.taf.shuvayatra.presenter;

import com.taf.data.utils.Logger;
import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.DefaultSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Country;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.activity.OnBoardActivity;
import com.taf.shuvayatra.ui.fragment.DestinationFragment;
import com.taf.shuvayatra.ui.views.CountryView;
import com.taf.shuvayatra.ui.views.MvpView;
import com.taf.util.MyConstants;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Presenter for country list as found in
 * <a href="api.shuvayatra.org/api/">Country API call</a>
 * <p>
 * Simply lists out available countries, nothing more.
 *
 * @see OnBoardActivity
 * @see DestinationFragment
 */

public class CountryListPresenter implements Presenter {

    CountryView mView;
    UseCase mUseCase;

    private static final String TAG = "CountryListPresenter";

    @Inject
    public CountryListPresenter(@Named(MyConstants.UseCase.CASE_COUNTRY_LIST) UseCase pUseCase) {
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
        mUseCase.execute(new CountrySubscriber(), pData);
    }

    @Override
    public void attachView(MvpView view) {
        mView = (CountryView) view;
    }

    private final class CountrySubscriber extends DefaultSubscriber<List<Country>> {

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
        public void onNext(List<Country> pT) {
            Logger.e(TAG, ">>> call to on next");
            mView.renderCountries(pT);
            onCompleted();
        }
    }
}
