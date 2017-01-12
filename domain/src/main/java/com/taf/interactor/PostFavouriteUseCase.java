package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.Post;
import com.taf.repository.IPostRepository;

import rx.Observable;

public class PostFavouriteUseCase extends UseCase<Boolean> {

    private final IPostRepository mRepository;

    public PostFavouriteUseCase(ThreadExecutor pThreadExecutor, PostExecutionThread
            pPostExecutionThread, IPostRepository pRepository) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = pRepository;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable(UseCaseData pData) {
        boolean status = pData.getBoolean(UseCaseData.FAVOURITE_STATE, false);
        Post post = (Post) pData.getSerializable(UseCaseData.POST);
        return mRepository.updateFavouriteCount(post, status);
    }
}
