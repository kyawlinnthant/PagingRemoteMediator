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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.klt.paging.model.MockVo
import com.klt.paging.view.item.DataItem

@Composable
fun MocksScreen(
    data: LazyPagingItems<MockVo>,
    modifier: Modifier = Modifier,
) {

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->

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
                Spacer(modifier = modifier.navigationBarsPadding())
            }
        }
    }
}


