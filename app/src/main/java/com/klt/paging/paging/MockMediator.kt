package com.klt.paging.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.klt.paging.database.db.MockDb
import com.klt.paging.database.entity.KeyEntity
import com.klt.paging.database.entity.MockDataEntity
import com.klt.paging.mock.MockService
import com.klt.paging.model.toEntity
import com.klt.paging.model.toKey
import kotlinx.coroutines.delay
import okio.IOException
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MockMediator @Inject constructor(
    private val service: MockService,
    private val source: MockDb
) : RemoteMediator<Int, MockDataEntity>() {

    private val start = 1
    override suspend fun initialize(): InitializeAction {
//        return InitializeAction.LAUNCH_INITIAL_REFRESH
        return if (hadData())
            InitializeAction.SKIP_INITIAL_REFRESH
        else InitializeAction.LAUNCH_INITIAL_REFRESH

//        return if (shouldSkipInitialRefresh())
//            InitializeAction.SKIP_INITIAL_REFRESH
//        else InitializeAction.LAUNCH_INITIAL_REFRESH

    }

    private suspend fun hadData() : Boolean{
        return source.mockDao().getMocks().isNotEmpty()
    }

    private suspend fun shouldSkipInitialRefresh(
        cacheTimeout: Long = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
    ): Boolean {
        return System.currentTimeMillis() - (source.keyDao().getCreationTime()
            ?: 0) < cacheTimeout
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MockDataEntity>
    ): MediatorResult {

        Log.e("shit.mediator.state","$loadType")
        val currentPage = getPage(
            loadType = loadType,
            state = state
        ) ?: return MediatorResult.Success(
            endOfPaginationReached = false
        )

        return try {
            delay(1000L)
            Log.e("shit.network","$currentPage , ${state.config.pageSize}")
            val response = service.getData(
                page = currentPage,
                size = state.config.pageSize
            )
            val endOfPagination = response.isEmpty()
            source.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    source.mockDao().deleteAll()
                    source.keyDao().deleteAll()
                }
                val prevKey = if (currentPage > start) currentPage - 1 else null
                val nextKey = if (endOfPagination) null else currentPage + 1
                val keys = response.map {
                    it.toKey(
                        nextPage = nextKey,
                        prevPage = prevKey,
                        currentPage = currentPage
                    )
                }
                val mocks = response.map {
                    it.toEntity()
                }
                source.keyDao().insertKeys(keys)
                source.mockDao().insertMocks(mocks)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getPage(
        loadType: LoadType,
        state: PagingState<Int, MockDataEntity>
    ): Int? {

        return when (loadType) {

            // loading
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextPage?.minus(1) ?: start
            }

            // has data, load more
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                remoteKeys?.nextPage
            }

            // has data, load previous
            LoadType.PREPEND -> {
                null
//                val remoteKeys = getFirstRemoteKey(state)
//                remoteKeys?.prevPage
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MockDataEntity>): KeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                source.keyDao().getKey(id)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, MockDataEntity>): KeyEntity? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { id ->
                source.keyDao().getKey(id.id)
            }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, MockDataEntity>): KeyEntity? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { id ->
                source.keyDao().getKey(id.id)
            }

    }
}