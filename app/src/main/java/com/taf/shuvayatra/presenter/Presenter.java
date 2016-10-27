package com.taf.shuvayatra.presenter;


import com.taf.interactor.UseCaseData;
import com.taf.shuvayatra.ui.views.MvpView;

public interface Presenter {
    void resume();

    void pause();

    void destroy();

    void initialize(UseCaseData pData);

    void attachView(MvpView view);
}
