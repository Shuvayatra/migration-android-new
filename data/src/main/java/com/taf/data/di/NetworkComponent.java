package com.taf.data.di;

import com.taf.data.api.ApiRequest;
import com.taf.data.entity.mapper.DataMapper;

import javax.inject.Singleton;

import dagger.Component;
import retrofit.Retrofit;

@Singleton
@Component(modules = NetworkModule.class)
public interface NetworkComponent {
    Retrofit getRetrofit();

    ApiRequest getApiRequest();

    DataMapper getDataMapper();

}
