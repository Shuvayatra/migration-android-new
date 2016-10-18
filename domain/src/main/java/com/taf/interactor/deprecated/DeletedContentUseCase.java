package com.taf.interactor.deprecated;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.repository.IBaseRepository;

import javax.inject.Inject;

import rx.Observable;

public class DeletedContentUseCase extends UseCase<Boolean> {

    private final IBaseRepository mRepository;

    @Inject
    public DeletedContentUseCase(IBaseRepository pRepository, ThreadExecutor
            pThreadExecutor, PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = pRepository;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable(UseCaseData pData) {
        return mRepository.getSingle(pData.getLong(UseCaseData.LAST_UPDATED, -1L));
    }
}
