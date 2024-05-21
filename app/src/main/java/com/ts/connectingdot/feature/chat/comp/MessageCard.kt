package com.ts.connectingdot.feature.chat.comp

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.streamliners.utils.DateTimeUtils
import com.streamliners.utils.DateTimeUtils.Format.*
import com.ts.connectingdot.R
import com.ts.connectingdot.domain.model.Message
import com.ts.connectingdot.helper.userInitialBasedProfileProfile
import com.ts.connectingdot.ui.comp.general.AsyncImage
import com.ts.connectingdot.ui.theme.Neutral50

@Composable
fun MessageCard(
    message: Message

) {

    Card (
    ){
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            
            Text(
                text = message.message,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )

            val formattedTime = remember {
                derivedStateOf {
                    DateTimeUtils.formatTime(
                        HOUR_MIN_12,
                        message.time.toDate().time
                    )
                }
            }

            Text(
                text = formattedTime.value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )

        }
    }

}