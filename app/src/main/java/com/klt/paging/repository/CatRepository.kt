package com.klt.paging.repository

import androidx.paging.PagingData
import com.klt.paging.Cat
import kotlinx.coroutines.flow.Flow

interface CatRepository {
    fun getCats() : Flow<PagingData<Cat>>
}