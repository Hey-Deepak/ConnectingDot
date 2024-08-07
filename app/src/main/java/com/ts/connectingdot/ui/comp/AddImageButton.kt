package com.ts.connectingdot.ui.comp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AddImageButton(
    modifier: Modifier,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier
            .clip(CircleShape)
            .size(100.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp)
            .clickable {
                onClick()
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AddAPhoto,
            contentDescription = "Add Photo",
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.size(12.dp))
        
        Text(text = "Add Photo",
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center

        )
    }

}