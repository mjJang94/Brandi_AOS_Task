package com.mj.daum_image_search.module

import com.google.gson.GsonBuilder
import com.mj.daum_image_search.api.RetrofitConnection
import com.mj.daum_image_search.common.Constant
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



fun retrofitApiConnectionModule() = module{

    single {
        Retrofit.Builder()
            .baseUrl(Constant.KAKAO_SEARCH_HOST)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
            .create(RetrofitConnection::class.java)
    }
}





