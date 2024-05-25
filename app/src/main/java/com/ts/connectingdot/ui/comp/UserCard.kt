package com.ts.connectingdot.ui.comp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ts.connectingdot.R
import com.ts.connectingdot.domain.model.User
import com.ts.connectingdot.helper.userInitialBasedProfileProfile
import com.ts.connectingdot.ui.comp.general.AsyncImage
import com.ts.connectingdot.ui.theme.Neutral50

@Composable
fun UserCard(
    user: User,
    onClick:() -> Unit
) {
    Card (
        onClick = onClick
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){

            AsyncImage(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                uri = user.profileImageUrl ?: userInitialBasedProfileProfile(user.name),
                placeHolder = painterResource(id = R.drawable.ic_person)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = user.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black

                )
                Text(text = user.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Neutral50

                )
            }

        }
    }
}