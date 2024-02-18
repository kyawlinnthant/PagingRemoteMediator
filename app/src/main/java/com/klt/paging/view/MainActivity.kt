package com.klt.paging.view

import android.os.Bundle
import android.util.Log
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
    

    cats.apply {
        Log.e("aeiou.screen", "$loadState")
        if (loadState.mediator == null) {
             FirstTimeLoading()
            return
        }

        if (loadState.mediator!!.refresh is LoadState.Error) {
             FirstTimeError(message = "error") {

            }
            return
        }

        ListScreen(cats = cats.itemSnapshotList.items.map { it.toVo() }, loadStates = loadState.mediator!! )

//        this.loadState.refresh
//        this.loadState.append
//        this.loadState.prepend
//        this.loadState.mediator
//        this.loadState.source
//
//        // refresh
//        loadState.refresh.endOfPaginationReached
//        // append
//        loadState.append.endOfPaginationReached
//        // prepend
//        loadState.prepend.endOfPaginationReached
//        // mediator
//        loadState.mediator?.refresh
//        loadState.mediator?.append
//        loadState.mediator?.prepend
//        // source
//        loadState.source.refresh
//        loadState.source.append
//        loadState.source.prepend

    }
}


//CombinedLoadStates(
//refresh=Loading(endOfPaginationReached=false),
//prepend=NotLoading(endOfPaginationReached=false),
//append=NotLoading(endOfPaginationReached=false),
//source=LoadStates(
//        refresh=Loading(endOfPaginationReached=false),
//        prepend=NotLoading(endOfPaginationReached=false),
//        append=NotLoading(endOfPaginationReached=false)
//),
//mediator=null
//)
//
//CombinedLoadStates(
//refresh=NotLoading(endOfPaginationReached=false),
//prepend=NotLoading(endOfPaginationReached=false),
//append=NotLoading(endOfPaginationReached=false),
//source=LoadStates(
//    refresh=Loading(endOfPaginationReached=false),
//    prepend=NotLoading(endOfPaginationReached=false),
//    append=NotLoading(endOfPaginationReached=false)),
//mediator=LoadStates(
//    refresh=NotLoading(endOfPaginationReached=false),
//    prepend=NotLoading(endOfPaginationReached=false),
//    append=NotLoading(endOfPaginationReached=false))
//)
//
//CombinedLoadStates(
//    refresh=NotLoading(endOfPaginationReached=false),
//    prepend=NotLoading(endOfPaginationReached=false),
//    append=NotLoading(endOfPaginationReached=false),
//    source=LoadStates(
//        refresh=NotLoading(endOfPaginationReached=false),
//        prepend=NotLoading(endOfPaginationReached=true),
//        append=NotLoading(endOfPaginationReached=false)),
//    mediator=LoadStates(
//        refresh=NotLoading(endOfPaginationReached=false),
//        prepend=NotLoading(endOfPaginationReached=false),
//        append=NotLoading(endOfPaginationReached=false))
//)
