package com.taf.interactor.deprecated;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.repository.deprecated.ITagRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class GetTagListUseCase extends UseCase<List<String>> {

    private final ITagRepository mRepository;

    @Inject
    public GetTagListUseCase(ITagRepository pRepository, ThreadExecutor pThreadExecutor,
                             PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = pRepository;
    }

    @Override
    protected Observable<List<String>> buildUseCaseObservable(UseCaseData pData) {
        return mRepository.getList(-1, 0);
    }
}
