package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.repository.IPostRepository;

import rx.Observable;

public class UpdateFavouriteStateUseCase extends UseCase<Boolean> {

    private final IPostRepository mRepository;
    private final Long mId;

    public UpdateFavouriteStateUseCase(Long pId, ThreadExecutor pThreadExecutor, PostExecutionThread
            pPostExecutionThread, IPostRepository pRepository) {
        super(pThreadExecutor, pPostExecutionThread);
        mId = pId;
        mRepository = pRepository;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable(UseCaseData pData) {
        boolean status = pData.getBoolean(UseCaseData.FAVOURITE_STATE, false);
        return mRepository.updateFavouriteState(mId, status);
    }
}
