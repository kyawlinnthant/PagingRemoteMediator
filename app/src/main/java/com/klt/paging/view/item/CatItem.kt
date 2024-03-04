package com.klt.paging.view.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.klt.paging.R
import com.klt.paging.model.CatVo

@Composable
fun CatItem(
    cat: CatVo,
    modifier: Modifier = Modifier
) {
    val ratio = cat.width.toFloat() / cat.height.toFloat()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(
                ratio = ratio,
                matchHeightConstraintsFirst = true
            ),
        contentAlignment = Alignment.BottomCenter
    ) {

        AsyncImage(
            model = cat.photo,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            placeholder = painterResource(id = R.drawable.ic_launcher_background)
        )
        Text(
            text = "$cat",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

    }
}