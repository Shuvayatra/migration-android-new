package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.repository.IPostRepository;

import rx.Observable;

/**
 * Created by Nirazan-PC on 5/5/2016.
 */
public class UpdatePostShareCountUseCase extends UseCase {

    Long mId;
    IPostRepository mRepository;

    public UpdatePostShareCountUseCase(Long pId, IPostRepository pRepository,ThreadExecutor pThreadExecutor, PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mId = pId;
        mRepository = pRepository;
    }

    @Override
    protected Observable buildUseCaseObservable(UseCaseData pData) {
        return mRepository.updateUnSyncedShareCount(mId);
    }
}
