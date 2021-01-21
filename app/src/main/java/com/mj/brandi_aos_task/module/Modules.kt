package com.mj.brandi_aos_task.module

import com.google.gson.GsonBuilder
import com.mj.brandi_aos_task.api.RetrofitConnection
import com.mj.brandi_aos_task.common.Constant
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


