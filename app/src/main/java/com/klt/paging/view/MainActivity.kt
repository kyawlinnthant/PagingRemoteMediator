package com.klt.paging.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.klt.paging.database.CatEntity
import com.klt.paging.paging.FullScreenState
import com.klt.paging.paging.PagingItemFullScreenState
import com.klt.paging.paging.asLoadState
import com.klt.paging.theme.PagingTheme
import com.klt.paging.view.fullscreen.CatListScreen
import com.klt.paging.view.fullscreen.EmptyList
import com.klt.paging.view.fullscreen.FirstTimeError
import com.klt.paging.view.fullscreen.FirstTimeLoading
import com.klt.paging.view.item.CatErrorItem
import com.klt.paging.view.item.CatLoadingItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: MainViewModel = hiltViewModel()
            val cats = vm.cats.collectAsLazyPagingItems()
            PagingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent(cats = cats)
                }
            }
        }
    }
}

@Composable
fun MainContent(
    cats: LazyPagingItems<CatEntity>
) {

    val lazyListState = rememberLazyListState()

    cats.apply {

        when(loadState.refresh){
            is LoadState.Error -> FirstTimeError(message = "First time error") {

            }
            LoadState.Loading -> FirstTimeLoading()
            is LoadState.NotLoading -> Unit
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState
        ) {

            items(
                count = cats.itemCount,
            ) {
                val currentItem = cats[it]
                currentItem?.let { cat ->
                    Text(
                        text = cat.id,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    )
                }

            }

            when  {
                loadState.refresh is LoadState.Loading -> item {
                    CatLoadingItem()
                }
                loadState.append is LoadState.Error -> item {
                    CatErrorItem(message = "this is error") {
                        
                    }
                }
            }
        }
    }



//    val state = cats.asLoadState()
//    when (val type = state as FullScreenState) {
//        PagingItemFullScreenState.EmptyList -> EmptyList()
//        is PagingItemFullScreenState.FirstTimeError -> FirstTimeError(
//            message = type.message,
//            onRetry = {})
//
//        PagingItemFullScreenState.FirstTimeLoading -> FirstTimeLoading()
//        is PagingItemFullScreenState.ShowList -> CatListScreen(
//            cats = cats.itemSnapshotList.items,
//            itemState = type.itemState
//        )
//    }
}








