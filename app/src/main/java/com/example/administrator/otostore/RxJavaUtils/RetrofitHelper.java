package com.example.administrator.otostore.RxJavaUtils;


import com.mchsdk.paysdk.retrofitutils.okhttp.OkHttpHelper;
import com.mchsdk.paysdk.retrofitutils.retrofit.StringConverterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitHelper {
    private static Retrofit retrofit;


    private RetrofitHelper() {
    }

    static {
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .client(OkHttpHelper.getClient())
                .addConverterFactory(StringConverterFactory.create()) //String 转换
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .validateEagerly(true)
                .build();
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

}
