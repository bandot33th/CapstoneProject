package com.example.capstoneproject.data.remote.main

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        fun getApiService(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
            val retrofit = Retrofit.Builder()
                .baseUrl("http://34.101.243.9:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}