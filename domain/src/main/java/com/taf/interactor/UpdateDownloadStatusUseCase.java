package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.repository.IPostRepository;

import javax.inject.Inject;

import rx.Observable;

public class UpdateDownloadStatusUseCase extends UseCase<Boolean> {

    private final IPostRepository mRepository;

    @Inject
    public UpdateDownloadStatusUseCase(IPostRepository pRepository, ThreadExecutor
            pThreadExecutor, PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = pRepository;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable(UseCaseData pData) {
        Long reference = pData.getLong(UseCaseData.DOWNLOAD_REFERENCE);
        boolean status = pData.getBoolean(UseCaseData.DOWNLOAD_STATUS, false);

        if (reference == null) {
            throw new IllegalStateException("download reference not provided.");
        }

        return mRepository.updateDownloadStatus(reference, status);
    }
}
