package com.klt.paging.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.klt.paging.Cat
import com.klt.paging.paging.CatPagingSource
import com.klt.paging.paging.CatRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CatRepositoryImpl @Inject constructor(
    private val config: PagingConfig,
    private val remoteMediator: CatRemoteMediator,
    private val pagingSource: CatPagingSource,
) : CatRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getCats(): Flow<PagingData<Cat>> {

        return Pager(
            config = config,
            initialKey = 0,
            pagingSourceFactory = { pagingSource },
            remoteMediator = remoteMediator,
        ).flow
    }
}