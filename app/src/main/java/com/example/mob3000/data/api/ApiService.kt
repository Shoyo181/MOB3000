package com.example.mob3000.data.api

import com.example.mob3000.data.models.ApiData.ApiData
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {
    @GET("api/result/{resultId}/{language}")
    suspend fun getResults(@Path("resultId") resultId: String,
                           @Path("language") language: String
    ): ApiData
}
