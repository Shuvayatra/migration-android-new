package com.taf.shuvayatra.ui.interfaces;

import android.content.Context;

public interface MvpView {
    void showErrorView(String pErrorMessage);
    Context getContext();
}
