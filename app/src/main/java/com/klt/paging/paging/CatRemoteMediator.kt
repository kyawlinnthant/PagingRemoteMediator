package com.klt.paging.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.klt.paging.database.CatDatabase
import com.klt.paging.database.CatEntity
import com.klt.paging.database.RemoteKeyEntity
import com.klt.paging.model.toEntity
import com.klt.paging.model.toRemoteKey
import com.klt.paging.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CatRemoteMediator @Inject constructor(
    private val apiService: ApiService,
    private val database: CatDatabase,
) : RemoteMediator<Int, CatEntity>() {

    override suspend fun initialize(): InitializeAction {
        val shouldFetch = shouldFetchInitialPage()
        Log.e("kyaw.mediator.initialize", "$shouldFetch")
        return if (shouldFetchInitialPage()) InitializeAction.LAUNCH_INITIAL_REFRESH else InitializeAction.SKIP_INITIAL_REFRESH
    }

    private suspend fun shouldFetchInitialPage(): Boolean {
        return database.catDao().queryCats().isEmpty()
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, CatEntity>
    ): MediatorResult {

        Log.e("kyaw.mediator.load1", "$loadType : $state")

        val currentPage = getPage(loadType, state) ?: return MediatorResult.Success(
            endOfPaginationReached = false
        )
        Log.e("kyaw.mediator.load2", "page  = $currentPage")

        return try {
            delay(20000L)
            val response = apiService.getCats(page = currentPage, size = state.config.pageSize)
            val isEndOfList = response.isEmpty()
            withContext(Dispatchers.IO) {
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.catDao().deleteAll()
                        database.remoteKeyDao().deleteAll()
                    }
                    val prevKey = if (currentPage == Constant.START_PAGE) null else currentPage - 1
                    val nextKey = if (isEndOfList) null else currentPage + 1
                    val keys = response.map {
                        it.toRemoteKey(
                            nextPage = nextKey,
                            prevPage = prevKey,
                            currentPage = currentPage
                        )
                    }
                    database.remoteKeyDao().addRemoteKeys(keys)
                    database.catDao().insertCats(response.map { it.toEntity() })
                }
            }
            MediatorResult.Success(endOfPaginationReached = isEndOfList)
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
        state: PagingState<Int, CatEntity>
    ): Int? {

        return when (loadType) {

            // loading
            LoadType.REFRESH -> {
                Log.e("kyaw.mediator.refresh", "$loadType")
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextPage ?: Constant.START_PAGE
            }

            // has data, load more
            LoadType.APPEND -> {
                Log.e("kyaw.mediator.append", "$loadType")
                val remoteKeys = getLastRemoteKey(state)
                remoteKeys?.nextPage
            }

            // has data, load previous
            LoadType.PREPEND -> {
                Log.e("kyaw.mediator.prepend", "$loadType")
                val remoteKeys = getFirstRemoteKey(state)
                remoteKeys?.prevPage
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, CatEntity>): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { catId ->
                Log.e("kyaw.mediator.refresh.key", catId)
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