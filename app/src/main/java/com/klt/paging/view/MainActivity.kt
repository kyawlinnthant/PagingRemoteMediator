package com.klt.paging.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.klt.paging.model.CatVo
import com.klt.paging.theme.PagingTheme
import com.klt.paging.view.fullscreen.CatListScreen
import com.klt.paging.view.fullscreen.FirstTimeError
import com.klt.paging.view.fullscreen.FirstTimeLoading
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

    // todo need to debug full screen error
    cats.apply {
        when (loadState.refresh) {
            is LoadState.Error -> {
                if (loadState.mediator != null) {
                    val error = (loadState.refresh as LoadState.Error).error.message
                    FirstTimeError(
                        message = error ?: "Something's wrong"
                    ) {}
                }
            }

            LoadState.Loading -> {
                if (loadState.mediator != null)
                    FirstTimeLoading()
            }

            is LoadState.NotLoading -> CatListScreen(cats = cats)
        }
    }

}







