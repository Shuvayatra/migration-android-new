package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.Post;
import com.taf.repository.IPostRepository;
import com.taf.util.MyConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class GetPostListUseCase extends UseCase<List<Post>> {

    private final IPostRepository mRepository;
    private final Long mParentId;
    private final String mPostType;
    private final boolean mFavouriteOnly;
    private final boolean mUnSyncedOnly;
    private final List<String> mExcludeTypes;
    private final String mTitle;
    private final List<String> mTags;

    @Inject
    public GetPostListUseCase(Long pParentId,
                              String pPostType, boolean pFavouriteOnly, boolean pUnSyncedOnly,
                              String pTitle, List<String> pTags,
                              List<String> pExcludeTypes, IPostRepository pRepository,
                              ThreadExecutor pThreadExecutor, PostExecutionThread
                                      pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mParentId = pParentId;
        mPostType = pPostType;
        mFavouriteOnly = pFavouriteOnly;
        mUnSyncedOnly = pUnSyncedOnly;
        mRepository = pRepository;
        mExcludeTypes = pExcludeTypes;
        mTags = pTags;
        mTitle = pTitle;
    }

    @Override
    protected Observable<List<Post>> buildUseCaseObservable(UseCaseData pData) {
        int offset = 0;
        int limit = -1;
        List<Long> excludeIdList = new ArrayList<>();
        if(pData!=null) {
            offset = pData.getInteger(UseCaseData.OFFSET, 0);
            limit = pData.getInteger(UseCaseData.LIMIT, -1);

            excludeIdList = (List<Long>) pData.getSerializable(UseCaseData.EXCLUDE_LIST);
        }
        if (mUnSyncedOnly) {
            return mRepository.getPostWithUnSyncedFavourites();
        } else if (mParentId != null) {
            return mRepository.getPostByCategory(mParentId, limit, offset, mPostType,
                    mExcludeTypes, excludeIdList);
        } else if (mFavouriteOnly) {
            return mRepository.getFavouriteList(limit, offset);
        } else if (mExcludeTypes != null) {
            return mRepository.getPostWithExcludes(limit, offset, mExcludeTypes);
        } else if (mTitle!=null){
            return mRepository.getPostsByTitle(limit,offset,mTitle);
        } else if (mTags!=null){
            return mRepository.getPostByTags(limit,offset,mTags);
        }
        else {
            return mRepository.getList(limit, offset);
        }
    }
}
