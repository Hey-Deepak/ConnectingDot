package com.ts.connectingdot.feature.editProfile.comp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.streamliners.pickers.media.PickedMedia

@Composable
fun ProfileImage(
    modifier: Modifier,
    pickedMedia: PickedMedia,
    onClick: () -> Unit,
) {

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(pickedMedia.uri)
            .crossfade(enable = true )
            .build(),
        contentDescription = "Profile Image",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.size(100.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    )

}