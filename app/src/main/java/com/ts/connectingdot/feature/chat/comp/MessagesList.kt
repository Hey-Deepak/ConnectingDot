package com.ts.connectingdot.feature.chat.comp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ts.connectingdot.domain.model.ext.id
import com.ts.connectingdot.feature.chat.ChatViewModel
import com.ts.connectingdot.feature.chat.ChatViewModel.ChatListItem

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
        items(data.chatListItems) { chatListItem ->

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = when (chatListItem) {
                    is ChatListItem.SendMessage -> Alignment.CenterEnd
                    is ChatListItem.ReceivedMessage -> Alignment.CenterStart
                    is ChatListItem.Date -> Alignment.Center
                }
            ) {
                when (chatListItem) {
                    is ChatListItem.Date -> {
                        Text(text = chatListItem.date)
                    }
                    is ChatListItem.ReceivedMessage -> {
                       MessageCard(message = chatListItem.message)
                    }
                    is ChatListItem.SendMessage -> {
                        MessageCard(message = chatListItem.message)
                    }
                }

            }

        }
    }

}
