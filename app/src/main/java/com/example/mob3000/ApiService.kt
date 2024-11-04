package com.example.mob3000

import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {
    @GET("api/result/{resultId}/no")
    suspend fun getResults(@Path("resultId") resultId: String): ApiResponse
}
