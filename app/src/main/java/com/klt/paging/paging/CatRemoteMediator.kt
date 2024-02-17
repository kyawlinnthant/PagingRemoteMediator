package com.klt.paging.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.klt.paging.Cat
import com.klt.paging.database.CatDatabase
import com.klt.paging.database.CatEntity
import com.klt.paging.database.RemoteKeyEntity
import com.klt.paging.network.ApiService
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CatRemoteMediator @Inject constructor(
    private val apiService: ApiService,
    private val database: CatDatabase
) : RemoteMediator<Int, CatEntity>() {

//    override suspend fun initialize(): InitializeAction {
//        return InitializeAction.LAUNCH_INITIAL_REFRESH
//    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, CatEntity>
    ): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }

            else -> {
                pageKeyData as Int
            }
        }

        try {
            val response = apiService.getCats(page = page, size = state.config.pageSize)
            val isEndOfList = response.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.catDao().deleteAll()
                    database.remoteKeyDao().deleteAll()
                }
                val prevKey = if (page == Constant.START_LOAD_PAGE) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.map {
                    it.toRemoteKey(nextPage = nextKey, prevPage = prevKey)
                }
                database.remoteKeyDao().addRemoteKeys(keys)
                database.catDao().insertCats(response.map { it.toEntity() })
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, CatEntity>): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextPage?.minus(1) ?: Constant.START_LOAD_PAGE
            }

            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextPage
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }

            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                remoteKeys?.prevPage ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, CatEntity>): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { catId ->
                database.remoteKeyDao().getRemoteKey(catId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, CatEntity>): RemoteKeyEntity? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { cat ->
                database.remoteKeyDao().getRemoteKey(cat.id)
            }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, CatEntity>): RemoteKeyEntity? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { cat ->
                database.remoteKeyDao().getRemoteKey(cat.id)
            }

    }
}