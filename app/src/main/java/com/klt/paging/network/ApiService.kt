package com.klt.paging.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("images/search")
    suspend fun getCatsWithResponse(
        @Query("limit") size: Int,
        @Query("page") page: Int
    ): Response<List<CatDTO>>

    @GET("images/search")
    suspend fun getCats(
        @Query("limit") size: Int,
        @Query("page") page: Int,
        @Query("size") resolution: String = "med", // full
        @Query("order") order: String = "DESC", // full
    ): List<CatDTO>
}