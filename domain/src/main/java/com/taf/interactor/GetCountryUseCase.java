package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.Country;
import com.taf.model.base.ApiQueryParams;
import com.taf.repository.ICountryRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by rakeeb on 10/25/16.
 */

public class GetCountryUseCase extends UseCase<List<Country>> {

    private final ICountryRepository mCountryRepository;

    @Inject
    public GetCountryUseCase(ICountryRepository iCountryRepository, ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mCountryRepository = iCountryRepository;
    }

    @Override
    protected Observable<List<Country>> buildUseCaseObservable(UseCaseData pData) {
        boolean useCache = false;
        if (pData != null)
            useCache = pData.getBoolean(UseCaseData.CACHED_DATA, false);
        return mCountryRepository.getCountryList(useCache);
    }
}
