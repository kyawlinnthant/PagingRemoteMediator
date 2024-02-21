package com.klt.paging.view.fullscreen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klt.paging.database.CatEntity
import com.klt.paging.paging.PagingLazyItemState
import com.klt.paging.view.item.CatEndItem
import com.klt.paging.view.item.CatErrorItem
import com.klt.paging.view.item.CatLoadingItem

@Composable
fun CatListScreen(
    cats: List<CatEntity>,
    itemState: PagingLazyItemState
) {
    val lazyListState = rememberLazyListState()

    Log.e("antibiotic", "${cats.size}")
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState
    ) {

        items(
            count = cats.size,
            key = { cats[it].id }
        ) {
            val currentItem = cats[it]
            Text(
                text = currentItem.id,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }

        when (itemState) {
            PagingLazyItemState.End -> item {
                CatEndItem()
            }

            is PagingLazyItemState.Error -> item {
                CatErrorItem(message = itemState.message) {

                }
            }

            PagingLazyItemState.Loading -> item {
                CatLoadingItem()
            }
        }

    }
}