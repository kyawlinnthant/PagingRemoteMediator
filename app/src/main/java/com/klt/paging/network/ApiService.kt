package com.klt.paging.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("images/search")
    suspend fun getCats(
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int
    ): Response<List<CatDTO>>
}