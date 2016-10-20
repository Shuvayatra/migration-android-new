package com.taf.interactor;

/**
 * Created by rakeeb on 10/20/16.
 */

public class TypeSubscriber<T> extends DefaultSubscriber<T> {

    public int type;

    public TypeSubscriber(int type) {
        this.type = type;
    }
}
