package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.Podcast;
import com.taf.repository.IPodcastRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class GetPodcastListUseCase extends UseCase<List<Podcast>> {

    private final IPodcastRepository mRepository;

    @Inject
    public GetPodcastListUseCase(IPodcastRepository repository, ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    protected Observable<List<Podcast>> buildUseCaseObservable(UseCaseData pData) {
        return mRepository.getPodcasts();
    }
}
