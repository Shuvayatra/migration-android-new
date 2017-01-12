package com.taf.interactor.deprecated;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.interactor.UseCase;
import com.taf.interactor.UseCaseData;
import com.taf.model.Block;
import com.taf.repository.INewsRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class GetNewsBlocksUseCase extends UseCase<List<Block>> {

    private final INewsRepository mNewsRepository;

    @Inject
    public GetNewsBlocksUseCase(INewsRepository repository, ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mNewsRepository = repository;
    }

    @Override
    protected Observable<List<Block>> buildUseCaseObservable(UseCaseData pData) {
        boolean noCache = false;
        if(pData != null) noCache = pData.getBoolean(UseCaseData.NO_CACHE, false);

        return mNewsRepository.getBlocks(noCache);
    }
}
