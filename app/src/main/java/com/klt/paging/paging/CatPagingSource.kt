package com.klt.paging.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.klt.paging.Cat
import com.klt.paging.network.ApiService
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class CatPagingSource @Inject constructor(
    private val apiService: ApiService
) : PagingSource<Int, Cat>() {
    override fun getRefreshKey(state: PagingState<Int, Cat>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cat> {

        val currentPage = params.key ?: 0

        try {
            val response = apiService.getCats(
                page = currentPage
            )
            val pageResponse = response.body()?.map {
                it.toVo()
            }
            val endOfPaginationReached = pageResponse.isNullOrEmpty()
            if (endOfPaginationReached) {
                return LoadResult.Page(
                    data = pageResponse.orEmpty(),
                    prevKey = null, //we use forward only,
                    nextKey = null
                )
            }
            return LoadResult.Page(
                data = pageResponse.orEmpty(),
                prevKey = null, //we use forward only,
                nextKey = currentPage + 1
            )


        } catch (e: Exception) {
            return LoadResult.Error(e)
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}