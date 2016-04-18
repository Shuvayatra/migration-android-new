package com.taf.shuvayatra.ui.interfaces;

import android.content.Context;

public interface LoadDataView extends MvpView {
    void showLoadingView();

    void hideLoadingView();

    void showErrorView(String pErrorMessage);

    Context getContext();
}
