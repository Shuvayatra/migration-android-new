package com.taf.shuvayatra.ui.deprecated.interfaces;

import android.content.Context;

public interface MvpView {
    void showErrorView(String pErrorMessage);
    Context getContext();
}
