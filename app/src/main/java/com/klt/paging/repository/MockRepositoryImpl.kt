package com.klt.paging.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.klt.paging.database.db.MockDb
import com.klt.paging.database.entity.MockDataEntity
import com.klt.paging.mock.MockService
import com.klt.paging.paging.Constant
import com.klt.paging.paging.MockMediator
import javax.inject.Inject

class MockRepositoryImpl @Inject constructor(
    private val service: MockService,
    private val source: MockDb
) : MockRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getData(): Pager<Int, MockDataEntity> {

        val dbSource = { source.mockDao().pagingSource() }

        val config = PagingConfig(
            pageSize = Constant.PAGE_SIZE,
            prefetchDistance = Constant.PAGE_SIZE.div(2),
            initialLoadSize = PagingConfig.MAX_SIZE_UNBOUNDED,
            jumpThreshold = 1,
            enablePlaceholders = true,
            maxSize = PagingConfig.MAX_SIZE_UNBOUNDED
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