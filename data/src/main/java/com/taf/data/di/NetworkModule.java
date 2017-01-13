package com.taf.data.di;

import com.taf.data.api.ApiRequest;
import com.taf.data.api.ApiService;
import com.taf.data.api.HeaderInterceptor;
import com.taf.data.entity.mapper.DataMapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dagger module that provides objects for API requests
 */
@Module
public class NetworkModule {

    String mBaseUrl;

    public NetworkModule(String pBaseUrl) {
        mBaseUrl = pBaseUrl;
    }

    @Provides
    @Singleton
    DataMapper provideDataMapper() {
        return new DataMapper();
    }

    @Provides
    OkHttpClient provideHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new HeaderInterceptor())
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient pOkHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(pOkHttpClient)
                .build();
    }

    @Provides
    @Singleton
    ApiRequest provideApiRequest(Retrofit pRetrofit) {
        return new ApiRequest(pRetrofit.create(ApiService.class));
    }
}
