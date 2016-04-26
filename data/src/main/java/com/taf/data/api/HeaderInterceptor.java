package com.taf.data.api;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.taf.data.BuildConfig;

import java.io.IOException;

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("accept", "application/json")
                .addHeader("token", BuildConfig.API_KEY)
                .build();
        Response response = chain.proceed(request);
        return response;
    }
}
