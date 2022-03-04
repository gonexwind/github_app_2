package com.fikky.githubuserapp.service.repository

import com.fikky.githubuserapp.service.util.Constant.BASE_URL
import com.fikky.githubuserapp.service.util.Constant.TOKEN
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {
    private fun client() =
        OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("Authorization", TOKEN)
                val request = requestBuilder.build()
                it.proceed(request)
            }
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

    fun create(): ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}