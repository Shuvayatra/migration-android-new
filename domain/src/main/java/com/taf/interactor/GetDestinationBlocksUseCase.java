package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.repository.ICountryRepository;

import rx.Observable;

/**
 * Created by umesh on 10/27/16.
 */

public class GetDestinationBlocksUseCase extends UseCase {

    ICountryRepository mRepository;
    long mId;

    public GetDestinationBlocksUseCase(long id,
                                       ICountryRepository repository,
                                       ThreadExecutor pThreadExecutor,
                                       PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);

        mRepository = repository;
        mId = id;
    }

    @Override
    protected Observable buildUseCaseObservable(UseCaseData pData) {
        return mRepository.getCountryBlocks(mId);
    }
}
