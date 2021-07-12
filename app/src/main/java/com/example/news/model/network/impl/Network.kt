package com.example.news.model.network.impl

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {

    // every API call needs the apiKey parameter, so we add an interceptor
    // to automatically add it to every single API call
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(ApiInterceptor())
            .addNetworkInterceptor(StethoInterceptor())  // for viewing network calls in Chrome Browser @ chrome://inspect
            .build()
    }

    private val builder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl("http://newsapi.org")
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
    }

    // Singleton Retrofit instance
    val retrofit: Retrofit by lazy {
        builder.build()
    }

    // Singleton Retrofit API instance
    val API: Api by lazy {
        createService(retrofit, Api::class.java)
    }
}
