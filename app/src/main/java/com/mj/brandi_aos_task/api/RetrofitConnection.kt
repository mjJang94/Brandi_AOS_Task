package com.mj.brandi_aos_task.api

import com.mj.brandi_aos_task.common.Constant
import com.mj.brandi_aos_task.reponse.ImageSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitConnection {
    @Headers("Authorization:" + Constant.KAKAO_AUTH_VALUE)
    @GET("v2/search/image")
    fun startImageSearch(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: String,
        @Query("size") size: String
    ): Call<ImageSearchResponse>
}



