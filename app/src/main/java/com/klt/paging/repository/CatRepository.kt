package com.klt.paging.repository

import androidx.paging.PagingData
import com.klt.paging.database.CatEntity
import kotlinx.coroutines.flow.Flow

interface CatRepository {
    fun getCats() : Flow<PagingData<CatEntity>>
}