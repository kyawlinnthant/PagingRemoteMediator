package com.klt.paging.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.klt.paging.database.CatEntity
import com.klt.paging.theme.PagingTheme
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
                    CatListScreen(cats = cats)
                }
            }
        }
    }
}

@Composable
fun CatListScreen(
    cats: LazyPagingItems<CatEntity>
) {
    Log.e("cats.item", "${cats.itemCount}")
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        items(
            count = cats.itemCount,
        ) {
            val currentItem = cats[it]!!

            Text(
                text = currentItem.id,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }

        cats.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {

                        FirstTimeLoading()
                    }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = cats.loadState.refresh as LoadState.Error

                    item {

                        FirstTimeError(message = error.error.message ?: "Error") {

                        }
                    }

                }

                loadState.append is LoadState.Loading -> {
                    item { CatLoadingItem() }
                }

                loadState.append is LoadState.Error -> {
                    val error = cats.loadState.append as LoadState.Error
                    item {
                        CatErrorItem(
                            message = error.error.message ?: "Error",
                            onRetry = {

                            }
                        )
                    }
                }
            }

            if (loadState.append.endOfPaginationReached) {
                item {
                    CatEndItem()
                }
            }
        }
    }

    /* cats.apply {
         when (loadState.refresh) {
             is LoadState.Loading -> {
                 FirstTimeLoading()
             }

             is LoadState.Error -> {
                 val error = cats.loadState.refresh as LoadState.Error

                 FirstTimeError(message = error.error.message ?: "Error") {

                 }

             }

             is LoadState.NotLoading -> {
                 LazyColumn(
                     modifier = Modifier.fillMaxSize()
                 ) {

                     items(
                         count = cats.itemCount,
                     ) {
                         val currentItem = cats[it]!!

                         Text(
                             text = currentItem.id,
                             style = MaterialTheme.typography.titleLarge,
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .padding(4.dp)
                         )

                     }


                     when (loadState.append) {
                         is LoadState.Loading -> {
                             item { CatLoadingItem() }
                         }

                         is LoadState.Error -> {
                             val error = cats.loadState.append as LoadState.Error
                             item {
                                 CatErrorItem(
                                     message = error.error.message ?: "Error",
                                     onRetry = {

                                     }
                                 )
                             }
                         }

                         is LoadState.NotLoading -> {
                             if (loadState.append.endOfPaginationReached) {
                                 item {
                                     CatEndItem()
                                 }
                             }
                         }
                     }

                 }
             }
         }
     }*/

}


@Composable
private fun CatLoadingItem(
    modifier: Modifier = Modifier

) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun CatErrorItem(
    modifier: Modifier = Modifier,
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message)
        OutlinedButton(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
private fun CatEndItem(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp), contentAlignment = Alignment.Center
    ) {
        Text(text = "-- END --")
    }
}
