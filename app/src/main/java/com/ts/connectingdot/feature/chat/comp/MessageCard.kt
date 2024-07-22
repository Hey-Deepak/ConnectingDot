package com.ts.connectingdot.feature.chat.comp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.streamliners.utils.DateTimeUtils
import com.streamliners.utils.DateTimeUtils.Format.Companion.HOUR_MIN_12
import com.ts.connectingdot.domain.model.Message
import com.ts.connectingdot.ui.comp.general.AsyncImage

@Composable
fun MessageCard(
    message: Message

) {

    Card(

    ) {

        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {

            message.mediaUrl?.let {
                AsyncImage(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .widthIn(
                        min = 100.dp,
                        max = 220.dp
                    ),
                    uri = it,
                    contentScale = ContentScale.FillWidth
                )
            }

            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = message.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.weight(0.5f)
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
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                )

            }
        }


    }

}