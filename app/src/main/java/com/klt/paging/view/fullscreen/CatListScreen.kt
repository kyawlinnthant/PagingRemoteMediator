package com.klt.paging.view.fullscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.klt.paging.model.CatVo
import com.klt.paging.view.item.CatEndItem
import com.klt.paging.view.item.CatErrorItem
import com.klt.paging.view.item.CatItem
import com.klt.paging.view.item.CatLoadingItem

@Composable
fun CatListScreen(
    cats: LazyPagingItems<CatVo>,
    modifier: Modifier = Modifier,
) {

    val lazyGridState = rememberLazyStaggeredGridState()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            LazyColumn(
                modifier = modifier.fillMaxSize(),
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(4.dp),

            ) {
                items(
                    items = cats.itemSnapshotList,
//                    key = cats.itemKey { it.id }
                ){ cat ->
                    cat?.let {
                        CatItem(cat = it)
                    }
                }
                val loadState = cats.loadState.mediator

                if (loadState?.refresh is LoadState.Loading) {
                    item { FirstTimeLoading() }

                }
                // first time error
                if (loadState?.refresh is LoadState.Error) {
                    val error = (loadState.refresh as LoadState.Error).error
                    item {

                        FirstTimeError(
                            message = error.message ?: "Something's wrong"
                        ) {
                            cats.refresh()
                        }
                    }
                }

                // loading item
                item {
                    if (loadState?.append is LoadState.Loading) {
                        CatLoadingItem()
                    }
                }
                // error item
                item {
                    if (loadState?.append is LoadState.Error) {
                        val error = (loadState.append as LoadState.Error).error.message
                        CatErrorItem(message = error ?: "Something's wrong") {
                            cats.retry()
                        }
                    }
                }
                // end item
                item {
                    if (loadState?.append != null) {
                        if (loadState.append.endOfPaginationReached)
                            CatEndItem()
                    }
                }
            }

          /*  LazyVerticalStaggeredGrid(
                modifier = modifier.fillMaxSize(),
                state = lazyGridState,
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                items(
                    items = cats.itemSnapshotList,
                ) { cat ->
                    cat?.let {
                        CatItem(cat = it)
                    }
                }

                val loadState = cats.loadState.mediator

                if (loadState?.refresh is LoadState.Loading) {
                    item { FirstTimeLoading() }

                }
                // first time error
                if (loadState?.refresh is LoadState.Error) {
                    val error = (loadState.refresh as LoadState.Error).error
                    item {

                        FirstTimeError(
                            message = error.message ?: "Something's wrong"
                        ) {
                            cats.refresh()
                        }
                    }
                }

                // loading item
                item {
                    if (loadState?.append is LoadState.Loading) {
                        CatLoadingItem()
                    }
                }
                // error item
                item {
                    if (loadState?.append is LoadState.Error) {
                        val error = (loadState.append as LoadState.Error).error.message
                        CatErrorItem(message = error ?: "Something's wrong") {
                            cats.retry()
                        }
                    }
                }
                // end item
                item {
                    if (loadState?.append != null) {
                        if (loadState.append.endOfPaginationReached)
                            CatEndItem()
                    }
                }

            }*/

        }
    }
}


