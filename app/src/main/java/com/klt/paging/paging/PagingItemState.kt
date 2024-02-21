package com.klt.paging.paging

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.klt.paging.database.CatEntity


sealed interface FullScreenState
sealed interface PagingItemFullScreenState {
    data object FirstTimeLoading : PagingItemFullScreenState, FullScreenState
    data class FirstTimeError(val message: String) : PagingItemFullScreenState, FullScreenState
    data class ShowList(val itemState: PagingLazyItemState) : PagingItemFullScreenState,
        FullScreenState

    data object EmptyList : PagingItemFullScreenState, FullScreenState
    data object Idle : PagingItemFullScreenState

}

sealed interface PagingLazyItemState {
    data object Loading : PagingLazyItemState
    data class Error(val message: String) : PagingLazyItemState
    data object End : PagingLazyItemState

}

//  todo : debug the state , this code still wrong
fun LazyPagingItems<CatEntity>.asLoadState(): PagingItemFullScreenState {
    val state = this.loadState
    // network has been fetched and end of pagination is true
    val isListEmpty = state.refresh is LoadState.NotLoading && state.refresh.endOfPaginationReached

    // db has been queried or (mediator has been resulted)
    val shouldShowList =
        state.source.refresh is LoadState.NotLoading || state.mediator?.refresh is LoadState.NotLoading

    // item loading
    val isLoadMore = state.mediator?.refresh is LoadState.Loading
    // item error
    val isError = state.mediator?.refresh is LoadState.Error
    val itemErrorMessage: String = if (isError)
        (state.mediator?.refresh as LoadState.Error).error.message ?: "Something Wrong"
    else "Something wrong"


    val isFirstTimeLoading =
        (state.source.refresh is LoadState.Loading && !state.refresh.endOfPaginationReached)
                || (state.source.refresh is LoadState.Loading &&
                !state.source.refresh.endOfPaginationReached)
                || (state.mediator?.refresh is LoadState.Loading)

    val isFirstTimeError = (state.refresh is LoadState.Error)
            || (state.source.refresh is LoadState.Error)
            || (state.mediator?.refresh is LoadState.Error)

    val firstTimeErrorMessage =
        if (isFirstTimeError) (state.mediator?.refresh as LoadState.Error).error.message
            ?: "Something Wrong"
        else "Something wrong"

    val isEnd =
        state.mediator?.append?.endOfPaginationReached ?: state.append.endOfPaginationReached


    return when {
        isFirstTimeLoading -> PagingItemFullScreenState.FirstTimeLoading
        isFirstTimeError -> PagingItemFullScreenState.FirstTimeError(message = firstTimeErrorMessage)
        isListEmpty -> PagingItemFullScreenState.EmptyList
        shouldShowList -> PagingItemFullScreenState.ShowList(
            itemState = when {
                isError -> PagingLazyItemState.Error(message = itemErrorMessage)
                isEnd -> PagingLazyItemState.End
                isLoadMore -> PagingLazyItemState.Loading
                else -> PagingLazyItemState.Loading
            }
        )

        else -> PagingItemFullScreenState.Idle

    }

}

