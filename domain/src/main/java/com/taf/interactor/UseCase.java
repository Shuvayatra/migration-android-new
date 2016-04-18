package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public abstract class UseCase<T> {

    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;
    private Subscription mSubscription = Subscriptions.empty();

    protected UseCase(ThreadExecutor pThreadExecutor, PostExecutionThread pPostExecutionThread) {
        mPostExecutionThread = pPostExecutionThread;
        mThreadExecutor = pThreadExecutor;
    }

    protected abstract Observable<T> buildUseCaseObservable(UseCaseData pData);

    public void execute(Subscriber pSubscriber, UseCaseData pData) {
        this.mSubscription = this.buildUseCaseObservable(pData)
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostExecutionThread.getScheduler())
                .subscribe(pSubscriber);
    }

    /**
     * UnSubscribes from current {@link Subscription}.
     */
    public void unSubscribe() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
