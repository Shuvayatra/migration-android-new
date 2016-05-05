package com.taf.shuvayatra.di.module;

import android.content.Context;

import com.taf.data.database.DatabaseHelper;
import com.taf.data.di.PerActivity;
import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.LatestContentRepository;
import com.taf.data.repository.NotificationRepository;
import com.taf.data.repository.PostRepository;
import com.taf.data.repository.SectionCategoryRepository;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.interactor.DownloadAudioUseCase;
import com.taf.interactor.GetLatestContentUseCase;
import com.taf.interactor.GetNotificationListUseCase;
import com.taf.interactor.GetPostListUseCase;
import com.taf.interactor.GetSectionCategoryUseCase;
import com.taf.interactor.SaveNotificationListUseCase;
import com.taf.interactor.SyncFavouritesUseCase;
import com.taf.interactor.UpdateDownloadStatusUseCase;
import com.taf.interactor.UpdateFavouriteStateUseCase;
import com.taf.interactor.UpdatePostViewCountUseCase;
import com.taf.interactor.UseCase;
import com.taf.repository.IBaseRepository;
import com.taf.repository.INotificationRepository;
import com.taf.repository.IPostRepository;
import com.taf.repository.ISectionRepository;
import com.taf.util.MyConstants;

import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

    Long mId = null;
    Long mParentId = null;
    MyConstants.DataParent mParentType;
    String mPostType;
    List<String> mExcludeTypes = null;
    boolean mFavouriteOnly = false;
    boolean mUnSyncedOnly = false;
    boolean mIsCategory = false;

    public DataModule() {
    }

    public DataModule(Long pId) {
        mId = pId;
    }

    public DataModule(boolean pFavouriteOnly, boolean pUnSyncedOnly) {
        mFavouriteOnly = pFavouriteOnly;
        mUnSyncedOnly = pUnSyncedOnly;
    }

    public DataModule(MyConstants.DataParent pParentType, boolean pIsCategory, Long pParentId) {
        mParentType = pParentType;
        mIsCategory = pIsCategory;
        mParentId = pParentId;
    }

    public DataModule(MyConstants.DataParent pParentType, boolean pIsCategory, Long pParentId,
                      List<String> pExcludeTypes) {
        mParentType = pParentType;
        mIsCategory = pIsCategory;
        mParentId = pParentId;
        mExcludeTypes = pExcludeTypes;
    }

    public DataModule(List<String> pExcludeTypes) {
        mExcludeTypes = pExcludeTypes;
    }

    public DataModule(Long pParentId, List<String> pExcludeTypes) {
        mParentId = pParentId;
        mExcludeTypes = pExcludeTypes;
    }

    public DataModule(Long pId, Long pParentId, MyConstants.DataParent pParentType, String
            pPostType) {
        mId = pId;
        mParentId = pParentId;
        mParentType = pParentType;
        mPostType = pPostType;
    }

    @Provides
    @PerActivity
    DataStoreFactory provideDataStoreFactory(Context pContext, DataMapper pDataMapper,
                                             DatabaseHelper pHelper) {
        return new DataStoreFactory(pContext, pDataMapper, pHelper);
    }

    @Provides
    @PerActivity
    @Named("latest")
    UseCase provideLatestContentUseCase(@Named("latest") IBaseRepository pDataRepository,
                                        ThreadExecutor pThreadExecutor, PostExecutionThread
                                                pPostExecutionThread) {
        return new GetLatestContentUseCase(pDataRepository, pThreadExecutor, pPostExecutionThread);
    }

    @Provides
    @PerActivity
    @Named("favouritesSync")
    UseCase provideFavouritesSyncUseCase(IPostRepository pDataRepository,
                                         ThreadExecutor pThreadExecutor, PostExecutionThread
                                                 pPostExecutionThread) {
        return new SyncFavouritesUseCase(pThreadExecutor, pPostExecutionThread, pDataRepository);
    }

    @Provides
    @PerActivity
    @Named("favouriteUpdate")
    UseCase provideFavouriteUpdateUseCase(IPostRepository pDataRepository,
                                          ThreadExecutor pThreadExecutor, PostExecutionThread
                                                  pPostExecutionThread) {
        return new UpdateFavouriteStateUseCase(mId, pThreadExecutor, pPostExecutionThread,
                pDataRepository);
    }

    @Provides
    @PerActivity
    @Named("latest")
    IBaseRepository provideLatestContentDataRepository(DataStoreFactory pDataStoreFactory,
                                                       DataMapper pDataMapper) {
        return new LatestContentRepository(pDataStoreFactory, pDataMapper);
    }

    @Provides
    @PerActivity
    @Named("postList")
    UseCase providePostListUseCase(IPostRepository pDataRepository, ThreadExecutor pThreadExecutor,
                                   PostExecutionThread pPostExecutionThread) {
        return new GetPostListUseCase(mParentType, mParentId, mPostType, mFavouriteOnly,
                mUnSyncedOnly, mExcludeTypes, pDataRepository, pThreadExecutor,
                pPostExecutionThread);
    }

    @Provides
    @PerActivity
    ISectionRepository provideSectionCategoryRepository(DataStoreFactory pDataStoreFactory,
                                                        DataMapper pDataMapper) {
        return new SectionCategoryRepository(pDataStoreFactory, pDataMapper);
    }

    @Provides
    @PerActivity
    IPostRepository providePostDataRepository(DataStoreFactory pDataStoreFactory, DataMapper
            pDataMapper) {
        return new PostRepository(pDataStoreFactory, pDataMapper);
    }

    @Provides
    @PerActivity
    @Named("sectionCategory")
    UseCase provideSectionCategoryUseCase(ISectionRepository pRepository,
                                          ThreadExecutor pThreadExecutor, PostExecutionThread
                                                  pPostExecutionThread) {
        return new GetSectionCategoryUseCase(mIsCategory, mParentId, mParentType, pRepository, pThreadExecutor,
                pPostExecutionThread);
    }

    @Provides
    @PerActivity
    @Named("download_start")
    UseCase provideDownloadStartUseCase(IPostRepository pDataRepository, ThreadExecutor
            pThreadExecutor, PostExecutionThread pPostExecutionThread) {
        return new DownloadAudioUseCase(mId, pDataRepository, pThreadExecutor,
                pPostExecutionThread);

    }

    @Provides
    @PerActivity
    @Named("download_complete")
    UseCase provideDownloadCompleteUseCase(IPostRepository pDataRepository, ThreadExecutor
            pThreadExecutor, PostExecutionThread pPostExecutionThread) {
        return new UpdateDownloadStatusUseCase(pDataRepository, pThreadExecutor,
                pPostExecutionThread);
    }

    @Provides
    @PerActivity
    INotificationRepository provideNotificationRepository(DataStoreFactory pDataStoreFactory, DataMapper
            pDataMapper) {
        return new NotificationRepository(pDataStoreFactory, pDataMapper);
    }

    @Provides
    @PerActivity
    @Named("notification")
    UseCase provideNotificationUseCase(INotificationRepository pRepository,
                                       ThreadExecutor pExecutor, PostExecutionThread pThread) {
        return new GetNotificationListUseCase(pRepository, pExecutor, pThread);
    }

    @Provides
    @PerActivity
    @Named("notification_received")
    UseCase provideSaveNotificationUseCase(INotificationRepository pRepository,
                                           ThreadExecutor pExecutor, PostExecutionThread pThread) {
        return new SaveNotificationListUseCase(pRepository, pExecutor, pThread);
    }

    @Provides
    @PerActivity
    @Named("view_count_update")
    UseCase providePostViewCountUseCase(IPostRepository pRepository,
                                        ThreadExecutor pThreadExecutor, PostExecutionThread
                                                pPostExecutionThread) {
        return new UpdatePostViewCountUseCase(mId, pRepository, pThreadExecutor, pPostExecutionThread);
    }
}
