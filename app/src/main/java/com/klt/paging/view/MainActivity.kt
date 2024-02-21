package com.klt.paging.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
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
import com.klt.paging.model.CatVo
import com.klt.paging.theme.PagingTheme
import com.klt.paging.view.fullscreen.FirstTimeError
import com.klt.paging.view.fullscreen.FirstTimeLoading
import com.klt.paging.view.item.CatEndItem
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
    cats: LazyPagingItems<CatVo>
) {

    val lazyListState = rememberLazyListState()

    cats.apply {

        if (loadState.refresh is LoadState.Error && loadState.mediator == null) {
            val error = (loadState.refresh as LoadState.Error).error.message
            FirstTimeError(
                message = error ?: "Something's wrong"
            ) {}
            return
        }
        if (loadState.refresh is LoadState.Loading && loadState.mediator == null) {
            FirstTimeLoading()
            return
        }


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                if(loadState.source.prepend is LoadState.Loading){
                    CatLoadingItem()
                }
            }
            items(
                count = cats.itemCount,
            ) {
                val currentItem = cats[it]
                currentItem?.let { cat ->
                    Text(
                        text = "${it+1}  = ${cat.id}",
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
        }
    }

}








