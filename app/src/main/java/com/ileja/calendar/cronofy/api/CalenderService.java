package com.ileja.calendar.cronofy.api;

import android.text.TextUtils;

import com.ileja.calendar.cronofy.Config;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.String.format;

/**
 * Created by chentao on 16/5/16.
 */
public class CalenderService {

    private CalenderService() { }

    public static CalenderApi createCalenderService(final String accessToken) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Config.URL.Request_Api_Url);

        if (!TextUtils.isEmpty(accessToken)) {

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request newReq = request.newBuilder()
                            .addHeader("Authorization", format("Bearer %s", accessToken))
                            .build();
                    return chain.proceed(newReq);
                }
            }).build();

            builder.client(client);
        }

        return builder.build().create(CalenderApi.class);
    }
}
