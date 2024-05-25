package com.ts.connectingdot.ui.comp.general

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import coil.compose.AsyncImage

@Composable
fun AsyncImage(
    modifier: Modifier,
    uri: String,
    onClick: (() -> Unit)? = null,
    placeHolder: Painter? = null,
    contentScale: ContentScale = ContentScale.FillBounds,
) {

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri)
            .crossfade(enable = true)
            .build(),
        contentDescription = "",
        contentScale = contentScale,
        modifier = modifier
            .run {
                onClick?.let { clickable { it() } } ?: this
            },
        placeholder = placeHolder
    )

}