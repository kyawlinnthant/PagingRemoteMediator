package com.klt.paging.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.klt.paging.database.CatDatabase
import com.klt.paging.database.CatEntity
import com.klt.paging.paging.CatRemoteMediator
import com.klt.paging.paging.Constant
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CatRepositoryImpl @Inject constructor(
    private val config: PagingConfig,
    private val remoteMediator: CatRemoteMediator,
    private val database: CatDatabase
) : CatRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getCats(): Flow<PagingData<CatEntity>> {

        val dbSource = { database.catDao().getAll() }
        return Pager(
//            config = config,
            config = PagingConfig(
                pageSize = Constant.PAGE_SIZE, // 10
//        enablePlaceholders = false,
                initialLoadSize = Constant.INITIAL_LOAD_SIZE, // 20
                maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
//        jumpThreshold = 1,
//        prefetchDistance = 5,
//        maxSize = Constant.PAGE_SIZE + (2 * Constant.PREFETCH_DISTANCE), //pageSize + (2 * prefetchDistance )
            ),
            initialKey = Constant.START_PAGE,
            remoteMediator = remoteMediator,
            pagingSourceFactory = dbSource,
        ).flow
    }
}