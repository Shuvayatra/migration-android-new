package com.taf.shuvayatra.di.module;

import android.content.Context;

import com.taf.data.database.DatabaseHelper;
import com.taf.data.di.PerActivity;
import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.ComponentRepository;
import com.taf.data.repository.DeletedContentRepository;
import com.taf.data.repository.HomeRepository;
import com.taf.data.repository.JourneyRepository;
import com.taf.data.repository.LatestContentRepository;
import com.taf.data.repository.NotificationRepository;
import com.taf.data.repository.PostRepository;
import com.taf.data.repository.SectionCategoryRepository;
import com.taf.data.repository.TagRepository;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.data.utils.AppPreferences;
import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.interactor.GetHomeBlocksUseCase;
import com.taf.interactor.GetWidgetComponentUseCase;
import com.taf.interactor.GetJourneyUseCase;
import com.taf.interactor.UseCase;
import com.taf.interactor.deprecated.DeletedContentUseCase;
import com.taf.interactor.deprecated.DownloadAudioUseCase;
import com.taf.interactor.deprecated.GetLatestContentUseCase;
import com.taf.interactor.deprecated.GetNotificationListUseCase;
import com.taf.interactor.deprecated.GetPostListUseCase;
import com.taf.interactor.deprecated.GetSectionCategoryUseCase;
import com.taf.interactor.deprecated.GetSimilarPostsUseCase;
import com.taf.interactor.deprecated.GetSinglePostUseCase;
import com.taf.interactor.deprecated.GetTagListUseCase;
import com.taf.interactor.deprecated.SaveNotificationListUseCase;
import com.taf.interactor.deprecated.SyncFavouritesUseCase;
import com.taf.interactor.deprecated.UpdateDownloadStatusUseCase;
import com.taf.interactor.deprecated.UpdateFavouriteStateUseCase;
import com.taf.interactor.deprecated.UpdatePostShareCountUseCase;
import com.taf.interactor.deprecated.UpdatePostViewCountUseCase;
import com.taf.repository.IBaseRepository;
import com.taf.repository.IHomeRepository;
import com.taf.repository.IJourneyRepository;
import com.taf.repository.INotificationRepository;
import com.taf.repository.IPostRepository;
import com.taf.repository.ISectionRepository;
import com.taf.repository.ITagRepository;
import com.taf.repository.IWidgetComponentRepository;
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
    List<String> mTags = null;
    boolean mFavouriteOnly = false;
    boolean mUnSyncedOnly = false;
    boolean mIsCategory = false;
    String mTitle = null;

    public static final String NAMED_COUNTRY_WIDGET = "country_widget";

    public DataModule() {
    }



    public DataModule(Long pId) {
        mId = pId;
    }

    public DataModule(String pTitle, List<String> pTags) {
        mTitle = pTitle;
        mTags = pTags;
    }

    public DataModule(Long pId, String pPostType, List<String> pTags) {
        mId = pId;
        mPostType = pPostType;
        mTags = pTags;
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
    @Named("deleted")
    UseCase provideDeleteContentUseCase(@Named("deleted") IBaseRepository pDataRepository,
                                        ThreadExecutor pThreadExecutor, PostExecutionThread
                                                pPostExecutionThread) {
        return new DeletedContentUseCase(pDataRepository, pThreadExecutor, pPostExecutionThread);
    }

    @Provides
    @PerActivity
    @Named("deleted")
    IBaseRepository provideDeletedContentRepository(DataStoreFactory pDataStoreFactory) {
        return new DeletedContentRepository(pDataStoreFactory);
    }

    @Provides
    @PerActivity
    @Named("similarPostList")
    UseCase provideSimilarPostListUseCase(IPostRepository pDataRepository, ThreadExecutor
            pThreadExecutor, PostExecutionThread pPostExecutionThread) {
        return new GetSimilarPostsUseCase(mPostType, mTags, mId, pDataRepository, pThreadExecutor,
                pPostExecutionThread);
    }

    @Provides
    @PerActivity
    @Named("postList")
    UseCase providePostListUseCase(IPostRepository pDataRepository, ThreadExecutor pThreadExecutor,
                                   PostExecutionThread pPostExecutionThread) {
        return new GetPostListUseCase(mParentId, mPostType, mFavouriteOnly,
                mUnSyncedOnly, mTitle, mTags, mExcludeTypes, pDataRepository, pThreadExecutor,
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

    @Provides
    @PerActivity
    @Named("share_count_update")
    UseCase providePostShareCountUseCase(IPostRepository pRepository,
                                         ThreadExecutor pThreadExecutor, PostExecutionThread pPostExecutionThread) {
        return new UpdatePostShareCountUseCase(mId, pRepository, pThreadExecutor, pPostExecutionThread);
    }

    @Provides
    @PerActivity
    ITagRepository provideTagDataRepository(DataStoreFactory pDataStoreFactory, DataMapper
            pDataMapper) {
        return new TagRepository(pDataStoreFactory, pDataMapper);
    }

    @Provides
    @PerActivity
    @Named("tagList")
    UseCase provideTagListUseCase(ITagRepository pTagRepository, ThreadExecutor pThreadExecutor,
                                  PostExecutionThread pPostExecutionThread) {
        return new GetTagListUseCase(pTagRepository, pThreadExecutor, pPostExecutionThread);
    }

    @Provides
    @PerActivity
    @Named("single_post")
    UseCase provideSinglePostUseCase(IPostRepository pRepository, ThreadExecutor pThreadExecutor,
                                     PostExecutionThread pPostExecutionThread) {
        return new GetSinglePostUseCase(mId, pRepository, pThreadExecutor, pPostExecutionThread);
    }

    /* new version injections */
    @Provides
    @PerActivity
    @Named("home")
    UseCase provideHomeUseCase(IHomeRepository pRepository, ThreadExecutor pThreadExecutor,
                               PostExecutionThread pPostExecutionThread) {
        return new GetHomeBlocksUseCase(pRepository, pThreadExecutor, pPostExecutionThread);
    }

    @Provides
    @PerActivity
    IHomeRepository provideHomeRepository(DataStoreFactory dataStoreFactory, DataMapper
            dataMapper) {
        return new HomeRepository(dataStoreFactory, dataMapper);
    }

    @Provides
    @PerActivity
    @Named(NAMED_COUNTRY_WIDGET)
    UseCase provideCountryWidgetUseCase(IWidgetComponentRepository pRepository, ThreadExecutor pThreadExecutor,
                                        PostExecutionThread pPostExecutionThread) {
        return new GetWidgetComponentUseCase(pRepository, pThreadExecutor, pPostExecutionThread);
    }

    @Provides
    @PerActivity
    @Named("journey")
    UseCase provideJourneyUseCase(IJourneyRepository repository,
                                  ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread){
        return new GetJourneyUseCase(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    IJourneyRepository provideIJourneyRepository(DataStoreFactory dataStoreFactory, DataMapper dataMapper){
        return  new JourneyRepository(dataStoreFactory, dataMapper);
    }

    @Provides
    @PerActivity
    IWidgetComponentRepository provideComponentRepository(DataStoreFactory factory, DataMapper mapper,AppPreferences preferences) {
        return new ComponentRepository(factory, mapper, preferences);
    }
}
