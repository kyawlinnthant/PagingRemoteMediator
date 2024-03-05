package com.klt.paging.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.filter
import androidx.paging.flatMap
import androidx.paging.map
import com.klt.paging.database.db.MockDb
import com.klt.paging.database.entity.MockDataEntity
import com.klt.paging.mock.MockService
import com.klt.paging.model.toVo
import com.klt.paging.paging.Constant
import com.klt.paging.paging.MockMediator
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MockRepositoryImpl @Inject constructor(
    private val service: MockService,
    private val source: MockDb
) : MockRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getData(): Pager<Int, MockDataEntity> {

        val dbSource = { source.mockDao().pagingSource() }


        val config = PagingConfig(
            pageSize = 50,
//            initialLoadSize = 1
//            prefetchDistance = 10,
//            initialLoadSize = 50,
//            jumpThreshold = 1,
//            enablePlaceholders = true,
//            maxSize = PagingConfig.MAX_SIZE_UNBOUNDED
        )

        val remoteMediator = MockMediator(
            service = service,
            source = source
        )

        return Pager(
            config = config,
            initialKey = Constant.START_PAGE,
            remoteMediator = remoteMediator,
            pagingSourceFactory = dbSource,
        )
    }
}