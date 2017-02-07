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
    long mId;

    @Inject
    public GetPostListUseCase(long id,
                              String filterParams, IPostRepository pRepository,
                              ThreadExecutor pThreadExecutor, PostExecutionThread
                                      pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = pRepository;
        mFilterParams = filterParams;
        mId = id;
    }

    @Override
    protected Observable<PostResponse> buildUseCaseObservable(UseCaseData pData) {
        int offset = 0;
        int limit = 15;
        int feedType = 0;
        if (pData != null) {
            offset = pData.getInteger(UseCaseData.OFFSET, 0);
            limit = pData.getInteger(UseCaseData.LIMIT, 15);
            feedType = pData.getInteger(UseCaseData.CONTENT_TYPE, 0);
        }

        return mRepository.getList(feedType, limit, offset, mFilterParams, mId);
    }
}
