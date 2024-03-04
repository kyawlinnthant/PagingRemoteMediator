package com.klt.paging.mock

interface MockService {
    suspend fun getData(
        page : Int,
        size : Int
    ) : List<MockDto>

    fun getTotal() : Int
}