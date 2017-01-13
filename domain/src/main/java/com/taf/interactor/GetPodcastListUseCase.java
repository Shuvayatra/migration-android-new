package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.PodcastResponse;
import com.taf.repository.IPodcastRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetPodcastListUseCase extends UseCase<PodcastResponse> {

    private final IPodcastRepository mRepository;

    @Inject
    public GetPodcastListUseCase(IPodcastRepository repository, ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    protected Observable<PodcastResponse> buildUseCaseObservable(UseCaseData pData) {
        int offset = pData.getInteger(UseCaseData.OFFSET);
        return mRepository.getPodcasts(offset);
    }
}
