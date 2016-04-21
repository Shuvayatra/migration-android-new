package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.Post;
import com.taf.repository.IPostRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class GetPostListUseCase extends UseCase<List<Post>> {

    private final IPostRepository mRepository;

    @Inject
    public GetPostListUseCase(IPostRepository pRepository, ThreadExecutor pThreadExecutor,
                              PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = pRepository;
    }

    @Override
    protected Observable<List<Post>> buildUseCaseObservable(UseCaseData pData) {
        int offset = pData.getInteger(UseCaseData.OFFSET, 0);
        int limit = pData.getInteger(UseCaseData.LIMIT, -1);
        String type = pData.getString(UseCaseData.POST_TYPE, null);
        if(type == null) {
            return mRepository.getList(limit, offset);
        }else{
            return mRepository.getListByType(type, limit, offset);
        }
    }
}
