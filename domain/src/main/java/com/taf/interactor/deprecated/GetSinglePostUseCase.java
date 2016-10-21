package com.taf.interactor.deprecated;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Post;
import com.taf.repository.deprecated.IPostRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Nirazan-PC on 5/6/2016.
 */
public class GetSinglePostUseCase extends UseCase<Post> {

    private final IPostRepository mRepository;
    Long mId;

    @Inject
    public GetSinglePostUseCase(Long pId, IPostRepository pRepository, ThreadExecutor pThreadExecutor, PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);

        mRepository = pRepository;
        mId = pId;
    }

    @Override
    protected Observable<Post> buildUseCaseObservable(UseCaseData pData) {
        return mRepository.getSingle(mId);
    }
}
