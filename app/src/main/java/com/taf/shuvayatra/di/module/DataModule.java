package com.taf.shuvayatra.di.module;

import android.content.Context;

import com.taf.data.cache.CacheImpl;
import com.taf.data.database.DatabaseHelper;
import com.taf.data.di.PerActivity;
import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.ChannelRepository;
import com.taf.data.repository.ComponentRepository;
import com.taf.data.repository.CountryRepository;
import com.taf.data.repository.HomeRepository;
import com.taf.data.repository.JourneyRepository;
import com.taf.data.repository.PodcastRepository;
import com.taf.data.repository.PostRepository;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.data.repository.deprecated.DeletedContentRepository;
import com.taf.data.repository.deprecated.LatestContentRepository;
import com.taf.data.repository.deprecated.NotificationRepository;
import com.taf.data.repository.deprecated.SectionCategoryRepository;
import com.taf.data.repository.deprecated.TagRepository;
import com.taf.data.utils.AppPreferences;
import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.interactor.GetChannelUseCase;
import com.taf.interactor.GetCountryUseCase;
import com.taf.interactor.GetDestinationBlocksUseCase;
import com.taf.interactor.GetHomeBlocksUseCase;
import com.taf.interactor.GetJourneyUseCase;
import com.taf.interactor.GetPodcastListUseCase;
import com.taf.interactor.GetPostDetailUseCase;
import com.taf.interactor.GetPostListUseCase;
import com.taf.interactor.GetSearchPostsUseCase;
import com.taf.interactor.GetWidgetComponentUseCase;
import com.taf.interactor.PostFavouriteUseCase;
import com.taf.interactor.PostShareUseCase;
import com.taf.interactor.SyncUserActionsUseCase;
import com.taf.interactor.UseCase;
import com.taf.interactor.deprecated.DeletedContentUseCase;
import com.taf.interactor.deprecated.DownloadAudioUseCase;
import com.taf.interactor.deprecated.GetLatestContentUseCase;
import com.taf.interactor.deprecated.GetNotificationListUseCase;
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
import com.taf.repository.IChannelRepository;
import com.taf.repository.ICountryRepository;
import com.taf.repository.IHomeRepository;
import com.taf.repository.IJourneyRepository;
import com.taf.repository.IPodcastRepository;
import com.taf.repository.IPostRepository;
import com.taf.repository.IWidgetComponentRepository;
import com.taf.repository.deprecated.INotificationRepository;
import com.taf.repository.deprecated.ISectionRepository;
import com.taf.repository.deprecated.ITagRepository;
import com.taf.util.MyConstants;

import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

    public static final String NAMED_COUNTRY_WIDGET = "country_widget";
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

    String mFilterParams;


    public DataModule() {
    }

    public DataModule(Long pId) {
        mId = pId;
    }

    public DataModule(String filterParams) {
        mFilterParams = filterParams;
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
                                             DatabaseHelper pHelper, CacheImpl cache) {
        return new DataStoreFactory(pContext, pDataMapper, pHelper, cache);
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
    UseCase provideFavouritesSyncUseCase(com.taf.repository.deprecated.IPostRepository pDataRepository,
                                         ThreadExecutor pThreadExecutor, PostExecutionThread
                                                 pPostExecutionThread) {
        return new SyncFavouritesUseCase(pThreadExecutor, pPostExecutionThread, pDataRepository);
    }

    @Provides
    @PerActivity
    @Named("favouriteUpdate")
    UseCase provideFavouriteUpdateUseCase(com.taf.repository.deprecated.IPostRepository pDataRepository,
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
    UseCase provideSimilarPostListUseCase(com.taf.repository.deprecated.IPostRepository
                                                  pDataRepository, ThreadExecutor
                                                  pThreadExecutor, PostExecutionThread pPostExecutionThread) {
        return new GetSimilarPostsUseCase(mPostType, mTags, mId, pDataRepository, pThreadExecutor,
                pPostExecutionThread);
    }

    @Provides
    @PerActivity
    @Named("postList")
    UseCase provideDeprecaPostListUseCase(com.taf.repository.deprecated.IPostRepository
                                                  pDataRepository, ThreadExecutor pThreadExecutor,
                                          PostExecutionThread pPostExecutionThread) {
        return new com.taf.interactor.deprecated.GetPostListUseCase(mParentId, mPostType,
                mFavouriteOnly,
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
    com.taf.repository.deprecated.IPostRepository providePostDataRepository(DataStoreFactory pDataStoreFactory, DataMapper
            pDataMapper) {
        return new com.taf.data.repository.deprecated.PostRepository(pDataStoreFactory, pDataMapper);
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
    UseCase provideDownloadStartUseCase(com.taf.repository.deprecated.IPostRepository pDataRepository, ThreadExecutor
            pThreadExecutor, PostExecutionThread pPostExecutionThread) {
        return new DownloadAudioUseCase(mId, pDataRepository, pThreadExecutor,
                pPostExecutionThread);

    }

    @Provides
    @PerActivity
    @Named("download_complete")
    UseCase provideDownloadCompleteUseCase(com.taf.repository.deprecated.IPostRepository pDataRepository, ThreadExecutor
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
    UseCase providePostViewCountUseCase(com.taf.repository.deprecated.IPostRepository pRepository,
                                        ThreadExecutor pThreadExecutor, PostExecutionThread
                                                pPostExecutionThread) {
        return new UpdatePostViewCountUseCase(mId, pRepository, pThreadExecutor, pPostExecutionThread);
    }

    @Provides
    @PerActivity
    @Named("share_count_update")
    UseCase providePostShareCountUseCase(com.taf.repository.deprecated.IPostRepository pRepository,
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
    UseCase provideSinglePostUseCase(com.taf.repository.deprecated.IPostRepository pRepository, ThreadExecutor pThreadExecutor,
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
    @Named("podcast_list")
    UseCase providePodcastListUseCase(IPodcastRepository pRepository, ThreadExecutor
            pThreadExecutor,
                                      PostExecutionThread pPostExecutionThread) {
        return new GetPodcastListUseCase(pRepository, pThreadExecutor, pPostExecutionThread);
    }

    @Provides
    @PerActivity
    IPodcastRepository providePodcastRepository(DataStoreFactory dataStoreFactory, DataMapper
            dataMapper) {
        return new PodcastRepository(mId, dataStoreFactory, dataMapper);
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
    IWidgetComponentRepository provideComponentRepository(DataStoreFactory factory, DataMapper mapper, AppPreferences preferences) {
        return new ComponentRepository(factory, mapper, preferences);
    }

    @Provides
    @PerActivity
    @Named(MyConstants.UseCase.CASE_COUNTRY_LIST)
    UseCase provideCountryUseCase(ICountryRepository pRepository, ThreadExecutor pThreadExecutor,
                                  PostExecutionThread pPostExecutionThread) {
        return new GetCountryUseCase(pRepository, pThreadExecutor, pPostExecutionThread);
    }

    @Provides
    @PerActivity
    ICountryRepository provideCountryRepository(DataStoreFactory factory, DataMapper mapper) {
        return new CountryRepository(factory, mapper);
    }


    @Provides
    @PerActivity
    @Named("journey")
    UseCase provideJourneyUseCase(IJourneyRepository repository,
                                  ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread) {
        return new GetJourneyUseCase(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    IJourneyRepository provideIJourneyRepository(DataStoreFactory dataStoreFactory, DataMapper dataMapper) {
        return new JourneyRepository(dataStoreFactory, dataMapper);
    }

    @Provides
    @PerActivity
    @Named("post_list")
    UseCase providePostListUseCase(IPostRepository pRepository, ThreadExecutor
            pThreadExecutor, PostExecutionThread pPostExecutionThread) {
        return new GetPostListUseCase(mFilterParams, pRepository, pThreadExecutor,
                pPostExecutionThread);
    }

    @Provides
    @PerActivity
    @Named("post")
    UseCase providePostDetailUseCase(IPostRepository pRepository, ThreadExecutor
            pThreadExecutor, PostExecutionThread pPostExecutionThread) {
        return new GetPostDetailUseCase(mId, pRepository, pThreadExecutor,
                pPostExecutionThread);
    }

    @Provides
    @PerActivity
    IPostRepository providePostRepository(DataStoreFactory dataStoreFactory, DataMapper
            dataMapper, AppPreferences preferences) {
        return new PostRepository(dataStoreFactory, dataMapper, preferences);
    }

    @Provides
    @PerActivity
    @Named("post_favourite")
    UseCase providePostFavouriteUseCase(IPostRepository pDataRepository,
                                        ThreadExecutor pThreadExecutor, PostExecutionThread
                                                pPostExecutionThread) {
        return new PostFavouriteUseCase(mId, pThreadExecutor, pPostExecutionThread,
                pDataRepository);
    }

    @Provides
    @PerActivity
    @Named("post_share")
    UseCase providePostShareUseCase(IPostRepository pDataRepository,
                                    ThreadExecutor pThreadExecutor, PostExecutionThread
                                            pPostExecutionThread) {
        return new PostShareUseCase(mId, pThreadExecutor, pPostExecutionThread, pDataRepository);
    }

    @Provides
    @PerActivity
    @Named("destination-blocks")
    UseCase provideDestinationBlocksUseCase(ICountryRepository repository,
                                            ThreadExecutor threadExecutor,
                                            PostExecutionThread postExecutionThread) {
        return new GetDestinationBlocksUseCase(mId, repository, threadExecutor, postExecutionThread);

    }

    @Provides
    @PerActivity
    @Named("actions_sync")
    UseCase proveUserActionsSyncUseCase(IPostRepository repository, ThreadExecutor threadExecutor,
                                        PostExecutionThread postExecutionThread) {
        return new SyncUserActionsUseCase(threadExecutor, postExecutionThread, repository);
    }


    @Provides
    @PerActivity
    @Named(MyConstants.UseCase.CASE_CHANNEL_LIST)
    UseCase provideChannelUseCase(IChannelRepository pRepository, ThreadExecutor pThreadExecutor,
                                  PostExecutionThread pPostExecutionThread){
        return new GetChannelUseCase(pRepository, pThreadExecutor, pPostExecutionThread);
    }


    @Provides
    @PerActivity
    IChannelRepository provideChannelRepository(DataStoreFactory factory, DataMapper mapper){
        return new ChannelRepository(factory, mapper);
    }

    @Provides
    @PerActivity
    @Named("search-posts")
    UseCase provideSearchPostsUseCase(IPostRepository repository,
                                      ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread){
        return new GetSearchPostsUseCase(repository,threadExecutor, postExecutionThread);
    }
}
