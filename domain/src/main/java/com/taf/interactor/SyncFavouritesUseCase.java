package com.taf.interactor;

import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.model.Post;
import com.taf.model.SyncData;
import com.taf.repository.IPostRepository;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class SyncFavouritesUseCase extends UseCase<Boolean> {

    private final IPostRepository mRepository;

    public SyncFavouritesUseCase(ThreadExecutor pThreadExecutor, PostExecutionThread
            pPostExecutionThread, IPostRepository pRepository) {
        super(pThreadExecutor, pPostExecutionThread);
        mRepository = pRepository;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable(UseCaseData pData) {
        List<Post> postList = (List<Post>) pData.getSerializable(UseCaseData.SYNC_LIST);
        if (postList == null) {
            throw new IllegalStateException("sync data must be provided.");
        }
        List<SyncData> syncDataList = new ArrayList<>();
        for (Post post : postList) {
            syncDataList.add(new SyncData(post.getId(),post.isFavourite()==null?null:(post.isFavourite()
                    ? SyncData.STATUS_LIKE
                    : SyncData.STATUS_DISLIKE),
                    post.getUnSyncedViewCount()));
            System.out.println("SyncFavouritesUseCase " + ""+post.getTitle() + " view count "+ post.getUnSyncedViewCount());
        }
        return mRepository.syncFavourites(syncDataList);
    }
}
