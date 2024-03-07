package com.klt.paging.view.fullscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.klt.paging.model.MockVo
import com.klt.paging.view.item.DataItem
import com.klt.paging.view.item.EndItem
import com.klt.paging.view.item.LoadingItem

@Composable
fun MocksScreen(
    data: LazyPagingItems<MockVo>,
    modifier: Modifier = Modifier,
) {

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = 0
    )
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                data.refresh()
            }) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "refresh")
            }
        },
        floatingActionButtonPosition = FabPosition.EndOverlay
    ) { paddingValues ->

        data.apply {
            if (loadState.refresh is LoadState.Loading && loadState.source.refresh is LoadState.Loading && loadState.mediator?.refresh is LoadState.Loading) {
                FirstTimeLoading()
            }

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    count = data.itemCount,
                    key = (data.itemKey { it.id })
                ) { index ->
                    val current = data[index]
                    current?.let {
                        if (it == data[0]) {
                            Box(modifier = modifier.statusBarsPadding())
                        }
                        DataItem(mock = it)
                    }
                }

                item {
                    if (loadState.mediator?.append is LoadState.Loading) {
                        LoadingItem()
                    }
                }

                item {
                    loadState.mediator?.let { state ->
                        if (state.append.endOfPaginationReached) {
                            EndItem()
                        }
                    }
                }

                item {
                    Spacer(modifier = modifier.navigationBarsPadding())
                }
            }
        }
    }
}


