package com.klt.paging.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.klt.paging.Cat
import com.klt.paging.database.CatDatabase
import com.klt.paging.database.RemoteKeyEntity
import com.klt.paging.network.ApiService
import okio.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CatRemoteMediator @Inject constructor(
    private val apiService: ApiService,
    private val database: CatDatabase
) : RemoteMediator<Int, Cat>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Cat>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {

                // loading state
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                // load old data
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                }
                // load more data
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }

            }
            // get the network response
            val networkResponse = apiService.getCats(page = currentPage)
            // network data to entity
            val networkData = networkResponse.body()?.map { it.toEntity() }
            // check network response to load more data according to key
            val endOfPaginationReached = networkData.isNullOrEmpty()
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            // database operations
            database.withTransaction {
                // reset the data, means request from start : page = 0
                if (loadType == LoadType.REFRESH) {
                    database.catDao().deleteAll()
                    database.remoteKeyDao().deleteAllRemoteKeys()
                }
                // save remote keys
                val remoteKeys = networkResponse.body()?.map { it.toRemoteKey(nextPage = nextPage) }
                database.catDao().insertCats(cats = networkData!!)
                database.remoteKeyDao().addRemoteKeys(remoteKeys = remoteKeys!!)

            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)


        } catch (e: Exception) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        }
    }

    private fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Cat>
    ): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let {
                database.remoteKeyDao().getRemoteKey(id = it)
            }
        }
    }

    private fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Cat>
    ): RemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let {
                database.remoteKeyDao().getRemoteKey(id = it.id)
            }
    }

    private fun getRemoteKeyForLastItem(
        state: PagingState<Int, Cat>
    ): RemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let {
                database.remoteKeyDao().getRemoteKey(id = it.id)
            }
    }

}