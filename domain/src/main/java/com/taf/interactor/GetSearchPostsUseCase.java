package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.PostResponse;
import com.taf.repository.IPostRepository;

import rx.Observable;

public class GetSearchPostsUseCase extends UseCase<PostResponse> {

    IPostRepository mRepository;

    public GetSearchPostsUseCase(IPostRepository repository,
                                 ThreadExecutor pThreadExecutor,
                                 PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = repository;
    }

    @Override
    protected Observable<PostResponse> buildUseCaseObservable(UseCaseData pData) {
        int offset = 0;
        int limit = 15;
        String search = "";
        String type = "";
        if (pData != null) {
            offset = pData.getInteger(UseCaseData.OFFSET, 0);
            limit = pData.getInteger(UseCaseData.LIMIT, 15);
            search = pData.getString(UseCaseData.SEARCH_QUERY, "");
            type = pData.getString(UseCaseData.SEARCH_TYPE, "");
        }
        System.out.println("search = " + search);
        return mRepository.getSearchPosts(limit, offset, search, type);
    }
}
