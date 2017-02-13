package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.Channel;
import com.taf.repository.IChannelRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by ngima on 11/3/16.
 */

public class GetChannelUseCase extends UseCase<List<Channel>> {

    final IChannelRepository mIChannelRepository;

    @Inject
    public GetChannelUseCase(IChannelRepository iChannelRepository, ThreadExecutor
            threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mIChannelRepository = iChannelRepository;
    }


    @Override
    protected Observable<List<Channel>> buildUseCaseObservable(UseCaseData pData) {
        return mIChannelRepository.getChannelList();
    }
}
