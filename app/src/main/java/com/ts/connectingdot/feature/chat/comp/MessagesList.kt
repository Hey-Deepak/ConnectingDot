package com.ts.connectingdot.feature.chat.comp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ts.connectingdot.domain.model.ext.id
import com.ts.connectingdot.feature.chat.ChatViewModel

@Composable
fun MessagesList(
    data: ChatViewModel.Data
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(data.channel.messages) { message ->
            val isSelfSend = data.user.id() == message.sender

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = if (isSelfSend) Alignment.CenterEnd else Alignment.CenterStart
            )
            {
                MessageCard(
                    message = message
                )
            }


        }
    }

}