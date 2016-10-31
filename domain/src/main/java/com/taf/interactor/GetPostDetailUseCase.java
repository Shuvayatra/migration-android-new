package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.Post;
import com.taf.repository.IPostRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetPostDetailUseCase extends UseCase<Post> {

    private final IPostRepository mRepository;
    private final Long mId;

    @Inject
    public GetPostDetailUseCase(Long id, IPostRepository pRepository,
                                ThreadExecutor pThreadExecutor, PostExecutionThread
                                        pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = pRepository;
        mId = id;
    }

    @Override
    protected Observable<Post> buildUseCaseObservable(UseCaseData pData) {
        return mRepository.getDetail(mId);
    }
}
