package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.Post;
import com.taf.repository.IPostRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class GetSimilarPostsUseCase extends UseCase<List<Post>> {

    private final IPostRepository mRepository;
    private final Long mExcludeId;
    private final List<String> mTags;
    private final String mPostType;

    @Inject
    public GetSimilarPostsUseCase(String pPostType, List<String> pTags, Long pExcludeId,
                                  IPostRepository pRepository, ThreadExecutor pThreadExecutor,
                                  PostExecutionThread pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mPostType = pPostType;
        mRepository = pRepository;
        mTags = pTags;
        mExcludeId = pExcludeId;
    }

    @Override
    protected Observable<List<Post>> buildUseCaseObservable(UseCaseData pData) {
        int offset = pData.getInteger(UseCaseData.OFFSET, 0);
        int limit = pData.getInteger(UseCaseData.LIMIT, 5);
        List<Long> excludeIdList = new ArrayList<>();
        if (mExcludeId != null)
            excludeIdList.add(mExcludeId);

        return mRepository.getSimilarPost(mPostType, mTags, excludeIdList, limit, offset);
    }
}
