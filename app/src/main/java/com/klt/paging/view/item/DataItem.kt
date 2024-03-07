package com.klt.paging.view.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.klt.paging.model.MockVo
import com.klt.paging.theme.PagingTheme

@Composable
fun DataItem(
    mock: MockVo,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        Text(
            text = mock.name,
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

    }
}

@Composable
@Preview
private fun Preview() {
    PagingTheme {
        Surface {
            DataItem(mock = MockVo(id = 0, name = "Name 0"))
        }
    }
}