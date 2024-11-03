package com.example.mob3000.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Big5ApiService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://bigfive-f9cymyeb1-rubynor.vercel")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: Big5Api = retrofit.create(Big5Api::class.java)
}