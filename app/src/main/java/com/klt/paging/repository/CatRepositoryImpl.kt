package com.klt.paging.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.klt.paging.database.CatDatabase
import com.klt.paging.database.CatEntity
import com.klt.paging.network.ApiService
import com.klt.paging.paging.CatRemoteMediator
import com.klt.paging.paging.Constant
import javax.inject.Inject

class CatRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val database: CatDatabase
) : CatRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getCats(): Pager<Int, CatEntity> {

        // todo : just define config as UI,UX and performance
        val dbSource = { database.catDao().pagingSource() }
        val config = PagingConfig(
            initialLoadSize = Constant.INITIAL_LOAD_SIZE, // 20
            pageSize = Constant.PAGE_SIZE, // 10
            maxSize = Constant.MAX_LOAD_SIZE, // 100
            jumpThreshold = 1,
            enablePlaceholders = true,
            prefetchDistance = Constant.PREFETCH_DISTANCE, // 2
        )
        val remoteMediator = CatRemoteMediator(
            apiService = apiService,
            database = database
        )
        return Pager(
            config = config,
            initialKey = Constant.START_PAGE,
            remoteMediator = remoteMediator,
            pagingSourceFactory = dbSource,
        )
    }
}