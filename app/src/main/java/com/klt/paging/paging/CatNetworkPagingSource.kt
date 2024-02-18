package com.klt.paging.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.klt.paging.model.Cat
import com.klt.paging.network.ApiService
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class CatNetworkPagingSource @Inject constructor(
    private val apiService: ApiService
) : PagingSource<Int, Cat>() {

    override fun getRefreshKey(state: PagingState<Int, Cat>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cat> {

        val currentPage = params.key ?: Constant.START_LOAD_PAGE

        return try {
            val response = apiService.getCats(
                page = currentPage,
                size = params.loadSize
            )
            LoadResult.Page(
                data = response.map { it.toVo() },
                prevKey = if (currentPage == Constant.START_LOAD_PAGE) null else currentPage - 1,
                nextKey = if (response.isEmpty()) null else currentPage + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

}