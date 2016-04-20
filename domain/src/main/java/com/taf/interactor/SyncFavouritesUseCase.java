package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.repository.IPostRepository;

import rx.Observable;

public class SyncFavouritesUseCase extends UseCase{

    private final IPostRepository mRepository;

    public SyncFavouritesUseCase(ThreadExecutor pThreadExecutor, PostExecutionThread
            pPostExecutionThread, IPostRepository pRepository) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = pRepository;
    }

    @Override
    protected Observable buildUseCaseObservable(UseCaseData pData) {
        Long id = pData.getLong(UseCaseData.ID, null);
        boolean isFavourite = pData.getBoolean(UseCaseData.FAVOURITE_STATE, false);
        if(id == null){
            throw new IllegalStateException("Post id must be provided.");
        }
        return mRepository.updateFavouriteState(id, isFavourite);
    }
}
