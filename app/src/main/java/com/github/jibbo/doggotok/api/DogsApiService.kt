package com.github.jibbo.doggotok.api

import com.github.jibbo.doggotok.data.Dog
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface DogsApiService {
    @GET("breeds/image/random")
    suspend fun getRandomDog(): Dog

    companion object {
        private const val BASE_URL = "https://dog.ceo/api/"

        fun create(): DogsApiService {
            val logger = HttpLoggingInterceptor().apply { level = Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DogsApiService::class.java)
        }
    }
}