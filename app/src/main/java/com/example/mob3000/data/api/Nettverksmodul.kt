package com.example.mob3000.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Nettverksmodul {
    private const val BASE_URL = "https://bigfive-f9cymyeb1-rubynor.vercel.app/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}