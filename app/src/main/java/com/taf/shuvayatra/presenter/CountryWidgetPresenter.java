package com.taf.shuvayatra.presenter;

import com.taf.exception.DefaultErrorBundle;
import com.taf.interactor.TypeSubscriber;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.CountryWidgetData;
import com.taf.model.CountryWidgetData.CalendarComponent;
import com.taf.shuvayatra.di.module.DataModule;
import com.taf.shuvayatra.exception.ErrorMessageFactory;
import com.taf.shuvayatra.ui.views.MvpView;
import com.taf.shuvayatra.ui.views.CountryWidgetView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * This presenter is for {@link CountryWidgetData} which contains logic for instantiation of
 * <ul>
 * <li>{@link CalendarComponent#COMPONENT_CALENDAR}</li>
 * <li>{@link CalendarComponent#COMPONENT_FOREX}</li>
 * <li>{@link CalendarComponent#COMPONENT_WEATHER}</li>
 * </ul>
 * <p>
 * To avoid multiple dagger injections, {@link UseCaseData#getInteger(String)} is used to
 * differentiate {@link CountryWidgetData.Component#componentType()}
 *
 * @see UseCaseData#COMPONENT_TYPE
 */

public class CountryWidgetPresenter implements Presenter {

    final UseCase mUseCase;
    CountryWidgetView mView;

    @Inject
    public CountryWidgetPresenter(@Named(DataModule.NAMED_COUNTRY_WIDGET) UseCase useCase) {
        mUseCase = useCase;
    }

    @Override
    public void resume() {
        // call fragment's resume?
    }

    @Override
    public void pause() {
        // call fragment's pause?
    }

    @Override
    public void destroy() {
        // call fragment's destroy
    }

    @Override
    public void initialize(UseCaseData pData) {
        mUseCase.execute(new CountryWidgetSubscriber(pData.getInteger(UseCaseData.COMPONENT_TYPE)),
                pData);
        mView.onLoadingView(pData.getInteger(UseCaseData.COMPONENT_TYPE));
    }

    @Override
    public void attachView(MvpView view) {
        mView = (CountryWidgetView) view;
    }


    // your subscriber for events
    private final class CountryWidgetSubscriber extends TypeSubscriber<CountryWidgetData.Component> {

        CountryWidgetSubscriber(int type) {
            super(type);
        }

        @Override
        public void onCompleted() {
            // do nothing for now
        }

        @Override
        public void onError(Throwable e) {
            mView.onErrorView(type, ErrorMessageFactory.create(mView.getContext(), new
                    DefaultErrorBundle((Exception) e).getException()));
        }

        @Override
        public void onNext(CountryWidgetData.Component component) {
            // save to cache??
            mView.onComponentLoaded(component);
            onCompleted();
        }
    }
}
