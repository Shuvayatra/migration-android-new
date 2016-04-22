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

    @Inject
    public GetPostListUseCase(MyConstants.DataParent pParentType, Long pParentId,
                              String pPostType, IPostRepository pRepository, ThreadExecutor
                                          pThreadExecutor, PostExecutionThread
                                          pPostExecutionThread) {
        super(pThreadExecutor, pPostExecutionThread);
        mParentType = pParentType;
        mParentId = pParentId;
        mPostType = pPostType;
        mRepository = pRepository;
    }

    @Override
    protected Observable<List<Post>> buildUseCaseObservable(UseCaseData pData) {
        int offset = pData.getInteger(UseCaseData.OFFSET, 0);
        int limit = pData.getInteger(UseCaseData.LIMIT, -1);
        boolean favouritesOnly = pData.getBoolean(UseCaseData.FAVOURITE_ONLY, false);

        if(mParentType != null){
            switch(mParentType){
                default:
                case COUNTRY:
                    return mRepository.getList(3, offset);
            }
        }else if(favouritesOnly){
            return mRepository.getFavouriteList(limit, offset);
        }else {
            return mRepository.getList(limit, offset);
        }
    }
}
