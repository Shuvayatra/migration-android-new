package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.repository.IPostRepository;

import rx.Observable;

public class SyncUserActionsUseCase extends UseCase<Boolean> {

    private final IPostRepository mRepository;

    public SyncUserActionsUseCase(ThreadExecutor pThreadExecutor, PostExecutionThread
            pPostExecutionThread, IPostRepository pRepository) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = pRepository;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable(UseCaseData pData) {
        return mRepository.syncUserActions();
    }
}
