package com.taf.interactor.deprecated;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.LatestContent;
import com.taf.repository.IBaseRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetLatestContentUseCase extends UseCase<LatestContent> {

    private final IBaseRepository mLatestContentRepository;

    @Inject
    public GetLatestContentUseCase(IBaseRepository pRepository, ThreadExecutor
            pThreadExecutor, PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mLatestContentRepository = pRepository;
    }

    @Override
    protected Observable<LatestContent> buildUseCaseObservable(UseCaseData pData) {
        return mLatestContentRepository.getSingle(pData.getLong(UseCaseData.LAST_UPDATED, -1L));
    }
}
