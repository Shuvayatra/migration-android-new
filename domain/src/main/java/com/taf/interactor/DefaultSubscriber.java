package com.taf.interactor;

import rx.Subscriber;

public class DefaultSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(T pT) {

    }
}
