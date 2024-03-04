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

        //  Maximum size must be at least pageSize + 2*prefetchDist, pageSize=10, prefetchDist=10, maxSize=20
        val dbSource = { database.catDao().pagingSource() }
        val config = PagingConfig(
//            initialLoadSize = 50, // 20
//            pageSize = 50, // 10
//            maxSize = 52, // 100
//            jumpThreshold = 1,
//            enablePlaceholders = true,
//            prefetchDistance = 1, // 2

            pageSize = 50,
//            prefetchDistance = 10,
//            initialLoadSize = 50,
            jumpThreshold = 1,
            enablePlaceholders = true,
            maxSize = PagingConfig.MAX_SIZE_UNBOUNDED
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