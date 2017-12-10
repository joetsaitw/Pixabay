package org.joetsai.pixabay

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {

    private val BASE_URL = "https://pixabay.com/api/"


    private val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE})
            .build()

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }

}