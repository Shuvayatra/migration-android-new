package com.taf.data.di;

import com.squareup.okhttp.OkHttpClient;
import com.taf.data.api.ApiRequest;
import com.taf.data.api.ApiService;
import com.taf.data.api.HeaderInterceptor;
import com.taf.data.entity.mapper.DataMapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

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
        OkHttpClient okClient = new OkHttpClient();
        okClient.interceptors().add(new HeaderInterceptor());
        return okClient;
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
