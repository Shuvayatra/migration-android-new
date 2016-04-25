package com.taf.shuvayatra.di.module;

import android.content.Context;

import com.taf.data.database.DatabaseHelper;
import com.taf.data.di.PerActivity;
import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.SectionCategoryRepository;
import com.taf.data.repository.LatestContentRepository;
import com.taf.data.repository.PostRepository;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.interactor.GetLatestContentUseCase;
import com.taf.interactor.GetPostListUseCase;
import com.taf.interactor.GetSectionCategoryUseCase;
import com.taf.interactor.SyncFavouritesUseCase;
import com.taf.interactor.UseCase;
import com.taf.repository.IBaseRepository;
import com.taf.repository.IPostRepository;
import com.taf.repository.ISectionRepository;
import com.taf.util.MyConstants;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

    Long mParentId = Long.MIN_VALUE;
    MyConstants.DataParent mParentType;
    String mPostType;
    boolean mFavouriteOnly = false;
    boolean mUnSyncedOnly = false;

    public DataModule() {
    }

    public DataModule(boolean pFavouriteOnly, boolean pUnSyncedOnly) {
        mFavouriteOnly = pFavouriteOnly;
        mUnSyncedOnly = pUnSyncedOnly;
    }

    public DataModule(Long pParentId, MyConstants.DataParent pParentType) {
        mParentId = pParentId;
        mParentType = pParentType;
    }

    public DataModule(Long pParentId, MyConstants.DataParent pParentType, String pPostType) {
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
                mUnSyncedOnly, pDataRepository, pThreadExecutor, pPostExecutionThread);
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
        return new GetSectionCategoryUseCase(pRepository, pThreadExecutor, pPostExecutionThread);
    }

}