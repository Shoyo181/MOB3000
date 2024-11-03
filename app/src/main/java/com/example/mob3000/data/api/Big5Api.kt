package com.example.mob3000.data.api

import com.example.mob3000.data.model.Big5TestRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Big5Api {
    @GET(".api/results/{id}/no")
    suspend fun getResults(@Path("id") testId: String): Response<Big5TestRes>
}