package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.PostResponse;
import com.taf.repository.IPostRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetPostListUseCase extends UseCase<PostResponse> {

    private final IPostRepository mRepository;
    private final String mFilterParams;

    @Inject
    public GetPostListUseCase(String filterParams, IPostRepository pRepository,
                              ThreadExecutor pThreadExecutor, PostExecutionThread
                                      pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = pRepository;
        mFilterParams = filterParams;
    }

    @Override
    protected Observable<PostResponse> buildUseCaseObservable(UseCaseData pData) {
        int offset = 0;
        int limit = 15;
        if (pData != null) {
            offset = pData.getInteger(UseCaseData.OFFSET, 0);
            limit = pData.getInteger(UseCaseData.LIMIT, 15);
        }

        return mRepository.getList(limit, offset, mFilterParams);
    }
}
