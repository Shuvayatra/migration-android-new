package com.taf.shuvayatra.di.module;

import android.content.Context;

import com.taf.data.database.DatabaseHelper;
import com.taf.data.di.PerActivity;
import com.taf.data.entity.mapper.DataMapper;
import com.taf.data.repository.LatestContentRepository;
import com.taf.data.repository.PostRepository;
import com.taf.data.repository.datasource.DataStoreFactory;
import com.taf.executor.PostExecutionThread;
import com.taf.executor.ThreadExecutor;
import com.taf.interactor.GetLatestContentUseCase;
import com.taf.interactor.GetPostListUseCase;
import com.taf.interactor.UseCase;
import com.taf.repository.IBaseRepository;
import com.taf.repository.IPostRepository;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

    private long mId = Long.MIN_VALUE;

    public DataModule() {
    }

    public DataModule(long pId) {
        mId = pId;
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
        return new GetPostListUseCase(pDataRepository, pThreadExecutor, pPostExecutionThread);
    }

    @Provides
    @PerActivity
    IPostRepository providePostDataRepository(DataStoreFactory pDataStoreFactory, DataMapper
            pDataMapper) {
        return new PostRepository(pDataStoreFactory, pDataMapper);
    }
}