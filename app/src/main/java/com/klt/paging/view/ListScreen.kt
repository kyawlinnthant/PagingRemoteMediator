package com.klt.paging.view

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import com.klt.paging.model.Cat

@Composable
fun ListScreen(
    cats: List<Cat>,
    loadStates : LoadStates
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        when(val refresh = loadStates.refresh)  {
             is LoadState.Error -> item {
                CatErrorItem(message = refresh.error.message ?: "") {

                }
            }

            LoadState.Loading -> item {
                CatLoadingItem()
            }

            is LoadState.NotLoading -> items(
                count = cats.size,
            ) {
                val currentItem = cats[it]

                Text(
                    text = currentItem.id,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

            }
        }

        when (loadStates.append) {
            is LoadState.Error -> TODO()
            LoadState.Loading -> TODO()
            is LoadState.NotLoading -> TODO()
        }

    }

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