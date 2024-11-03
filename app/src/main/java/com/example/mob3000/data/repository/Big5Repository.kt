package com.example.mob3000.data.repository

import com.example.mob3000.data.api.Big5ApiService
import com.example.mob3000.data.model.Big5TestRes
import com.example.mob3000.data.model.TestResultat
import retrofit2.Response

class Big5Repository {
    suspend fun getResults(testId: String): Response<Big5TestRes> {
        return Big5ApiService.api.getResults(testId)
    }
}