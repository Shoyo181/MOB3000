package com.example.mob3000.data.api

import com.example.mob3000.data.models.ScoreResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ScoreService {
    @GET("api/result/{testID}/no")
    suspend fun getScoreResults(@Path("testID") testID: String): ScoreResponse
}
