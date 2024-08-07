package com.ts.connectingdot.feature.editProfile.comp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ts.connectingdot.ui.comp.general.AsyncImage

@Composable
fun ProfileImage(
    modifier: Modifier,
    data: String,
    onClick: () -> Unit,
) {

    AsyncImage(
        modifier = modifier.size(100.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        uri = data
    )

}