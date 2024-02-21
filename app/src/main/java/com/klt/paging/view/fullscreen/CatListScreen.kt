package com.klt.paging.view.fullscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
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

    val lazyListState = rememberLazyListState()
    val lazyGridState = rememberLazyStaggeredGridState()

    cats.apply {

        Column(
            modifier = modifier.fillMaxSize(),
        ) {

            LazyVerticalStaggeredGrid(
                state = lazyGridState,
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    items(count = cats.itemCount) { index ->
                        val currentItem = cats[index]
                        currentItem?.let {

                            CatItem(index = index, cat = it)
                        }
                    }
                    item {
                        if (loadState.append is LoadState.Loading) {
                            CatLoadingItem()
                        }
                    }
                    item {
                        if (loadState.append is LoadState.Error) {
                            val error = (loadState.append as LoadState.Error).error.message
                            CatErrorItem(message = error ?: "Something's wrong") {
                                retry()
                            }
                        }
                    }
                    item {
                        if (loadState.append.endOfPaginationReached) {
                            CatEndItem()
                        }
                    }
                },
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }


        /*LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                if (loadState.source.prepend is LoadState.Loading) {
                    CatLoadingItem()
                }
            }
            items(
                count = cats.itemCount,
            ) {
                val currentItem = cats[it]
                currentItem?.let { cat ->
                    Text(
                        text = "${it + 1}  = ${cat.id}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

            }
            item {
                if (loadState.append is LoadState.Loading) {
                    CatLoadingItem()
                }
            }
            item {
                if (loadState.append is LoadState.Error) {
                    val error = (loadState.append as LoadState.Error).error.message
                    CatErrorItem(message = error ?: "Something's wrong") {}
                }
            }
            item {
                if (loadState.append.endOfPaginationReached) {
                    CatEndItem()
                }
            }
        }*/
    }

}
