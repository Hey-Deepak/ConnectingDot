package com.ts.connectingdot.feature.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.streamliners.compose.comp.textInput.TextInputLayout
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.compose.comp.textInput.state.ifValidInput
import com.streamliners.compose.comp.textInput.state.update
import com.ts.connectingdot.feature.chat.comp.MessagesList

@Composable
fun ChatScreen(
    channelId: String,
    navController: NavController,
    viewModel: ChatViewModel
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.start(channelId)
    }

    val messageInput = remember {
        mutableStateOf(
            TextInputState(
                label = "Messages",

                )
        )
    }

    TitleBarScaffold(
        title = "Chat",
        navigateUp = { navController.navigateUp() }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                viewModel.data.whenLoaded { data ->
                    MessagesList(data = data)
                }
            }

            TextInputLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                state = messageInput,
                trailingIconButton = {
                    IconButton(onClick = {
                        messageInput.ifValidInput { message ->
                            viewModel.sendMessage(message) {
                                messageInput.update("")
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send Button"
                        )
                    }
                }
            )

        }


    }

}