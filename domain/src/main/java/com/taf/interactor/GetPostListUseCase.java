package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.Post;
import com.taf.repository.IPostRepository;
import com.taf.util.MyConstants;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class GetPostListUseCase extends UseCase<List<Post>> {

    private final IPostRepository mRepository;
    private final MyConstants.DataParent mParentType;
    private final Long mParentId;
    private final String mPostType;
    private final boolean mFavouriteOnly;
    private final boolean mUnSyncedOnly;
    private final List<String> mExcludeTypes;

    @Inject
    public GetPostListUseCase(MyConstants.DataParent pParentType, Long pParentId,
                              String pPostType, boolean pFavouriteOnly, boolean pUnSyncedOnly,
                              List<String> pExcludeTypes, IPostRepository pRepository,
                              ThreadExecutor pThreadExecutor, PostExecutionThread
                                      pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mParentType = pParentType;
        mParentId = pParentId;
        mPostType = pPostType;
        mFavouriteOnly = pFavouriteOnly;
        mUnSyncedOnly = pUnSyncedOnly;
        mRepository = pRepository;
        mExcludeTypes = pExcludeTypes;
    }

    @Override
    protected Observable<List<Post>> buildUseCaseObservable(UseCaseData pData) {
        int offset = pData.getInteger(UseCaseData.OFFSET, 0);
        int limit = pData.getInteger(UseCaseData.LIMIT, -1);
        List<Long> excludeIdList = (List<Long>) pData.getSerializable(UseCaseData.EXCLUDE_LIST);

        if (mUnSyncedOnly) {
            return mRepository.getPostWithUnSyncedFavourites();
        } else if (mParentId != null) {
            return mRepository.getPostByCategory(mParentId, limit, offset, mPostType,
                    mExcludeTypes, excludeIdList);
        } else if (mFavouriteOnly) {
            return mRepository.getFavouriteList(limit, offset);
        } else if (mExcludeTypes != null) {
            return mRepository.getPostWithExcludes(limit, offset, mExcludeTypes);
        } else {
            return mRepository.getList(limit, offset);
        }
    }
}
