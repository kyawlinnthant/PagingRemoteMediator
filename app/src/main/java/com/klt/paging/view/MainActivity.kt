package com.klt.paging.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
                    CatListScreen(cats = cats)
                }
            }
        }
    }
}

@Composable
fun MainContent(
    cats: LazyPagingItems<CatVo>
) {

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            cats.apply {

                val firstTimeLoadState = this.loadState.mediator

                // first time Loading
                if (firstTimeLoadState?.refresh is LoadState.Loading) {
                    FirstTimeLoading()
                }
                // first time error
                if (firstTimeLoadState?.refresh is LoadState.Error) {
                    val error = (firstTimeLoadState.refresh as LoadState.Error).error
                    FirstTimeError(
                        message = error.message ?: "Something's wrong"
                    ) {
                        this.refresh()
                    }
                }

                CatListScreen(cats = this)
            }
        }
    }
}
