package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.Post;
import com.taf.model.PostResponse;
import com.taf.repository.INewsRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
@Deprecated
public class GetNewsBlocksUseCase extends UseCase<PostResponse> {

    private final INewsRepository mNewsRepository;

    @Inject
    public GetNewsBlocksUseCase(INewsRepository repository, ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mNewsRepository = repository;
    }

    @Override
    protected Observable<PostResponse> buildUseCaseObservable(UseCaseData pData) {
        boolean noCache = false;
        if(pData != null) noCache = pData.getBoolean(UseCaseData.NO_CACHE, false);

        return mNewsRepository.getNewsList(noCache, pData.getInteger(UseCaseData.OFFSET));
    }
}
