package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.repository.IPostRepository;

import javax.inject.Inject;

import rx.Observable;

public class DownloadAudioUseCase extends UseCase<Boolean> {

    private final IPostRepository mRepository;
    private long mId;

    @Inject
    public DownloadAudioUseCase(long pId, IPostRepository pRepository, ThreadExecutor
            pThreadExecutor, PostExecutionThread
                                        pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        this.mId = pId;
        mRepository = pRepository;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable(UseCaseData pData) {
        Long reference = pData.getLong(UseCaseData.DOWNLOAD_REFERENCE);
        if (reference == null) {
            throw new IllegalStateException("Download reference must ne provided.");
        }
        return mRepository.setDownloadReference(mId, reference);
    }
}
