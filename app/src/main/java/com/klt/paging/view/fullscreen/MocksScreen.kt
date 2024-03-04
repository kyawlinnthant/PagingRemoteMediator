package com.klt.paging.view.fullscreen

import android.util.Log
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.klt.paging.model.MockVo
import com.klt.paging.view.item.DataItem
import com.klt.paging.view.item.EndItem

@Composable
fun MocksScreen(
    data: LazyPagingItems<MockVo>,
    modifier: Modifier = Modifier,
) {

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->

        data.apply {
            val ans = this.takeIf {
                it.itemCount != 0
            }
            Log.e(
                "shit.screen",
                """                    ${this.itemCount}
                    ${this.itemSnapshotList.items.size} 
                    ${this.itemSnapshotList.size} 
                    ${this.loadState} """
            )

//            if (loadState.refresh is LoadState.Loading && loadState.mediator?.refresh is LoadState.Loading) {
//                FirstTimeLoading()
//            }

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    count = data.itemCount
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


