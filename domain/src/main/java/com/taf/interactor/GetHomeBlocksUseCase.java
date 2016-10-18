package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.Block;
import com.taf.repository.IHomeRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class GetHomeBlocksUseCase extends UseCase<List<Block>> {

    private final IHomeRepository mHomeRepository;

    @Inject
    public GetHomeBlocksUseCase(IHomeRepository repository, ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mHomeRepository = repository;
    }

    @Override
    protected Observable<List<Block>> buildUseCaseObservable(UseCaseData pData) {
        return mHomeRepository.getBlocks();
    }
}
