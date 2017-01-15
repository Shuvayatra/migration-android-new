package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.base.ApiQueryParams;
import com.taf.repository.IJourneyRepository;

import rx.Observable;

public class GetJourneyUseCase extends UseCase {

    private IJourneyRepository mRepository;

    public GetJourneyUseCase(IJourneyRepository journeyRepository,
                             ThreadExecutor pThreadExecutor,
                             PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = journeyRepository;
    }

    @Override
    protected Observable buildUseCaseObservable(UseCaseData pData) {
        return mRepository.getBlocks(new ApiQueryParams(pData));
    }
}
