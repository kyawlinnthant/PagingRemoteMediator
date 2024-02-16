package com.klt.paging

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.klt.paging.ui.theme.PagingTheme
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
    cats: LazyPagingItems<Cat>
) {
    cats.apply {
        Log.d("loadState", "$loadState")
    }


    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        // todo : check condition about loading state
        cats.apply {
            this.loadState.refresh
            this.loadState.append
            this.loadState.prepend
            this.loadState.mediator
            this.loadState.source

            // refresh
            loadState.refresh.endOfPaginationReached
            // append
            loadState.append.endOfPaginationReached
            // prepend
            loadState.prepend.endOfPaginationReached
            // mediator
            loadState.mediator?.refresh
            loadState.mediator?.append
            loadState.mediator?.prepend
            // source
            loadState.source.refresh
            loadState.source.append
            loadState.source.prepend

        }


        items(
            count = cats.itemCount,
        ) {
            val currentItem = cats[it]
            currentItem?.let { cat ->
                Text(
                    text = cat.photo,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        cats.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Text(
                            modifier = Modifier.fillParentMaxSize(),
                            text = "First Time Loading"
                        )
                    }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = cats.loadState.refresh as LoadState.Error
                    item {
                        Text(
                            modifier = Modifier.fillParentMaxSize(),
                            text = "First Time Error $error"
                        )
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item {
                        Text("loading")
                    }
                }

                loadState.append.endOfPaginationReached -> {
                    item {

                        Text(
                            text = "This is the end of our data",
                        )
                    }

                }
            }
        }

    }
}
