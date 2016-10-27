package com.taf.interactor.deprecated;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.repository.deprecated.IPostRepository;

import rx.Observable;

/**
 * Created by Nirazan-PC on 5/4/2016.
 */
public class UpdatePostViewCountUseCase extends UseCase {

    private final IPostRepository mRepository;
    private final Long mId;


    public UpdatePostViewCountUseCase(Long pId, IPostRepository pRepository,
                                      ThreadExecutor pThreadExecutor, PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = pRepository;
        mId = pId;
    }

    @Override
    protected Observable<Long> buildUseCaseObservable(UseCaseData pData) {
        return mRepository.updateUnSyncedViewCount(mId);
    }
}
