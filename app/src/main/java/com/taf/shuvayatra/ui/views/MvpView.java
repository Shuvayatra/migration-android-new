package com.taf.shuvayatra.ui.views;

import android.content.Context;

public interface MvpView {
    void showErrorView(String pErrorMessage);
    Context getContext();
}
