package com.ts.connectingdot.feature.newChat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.ts.connectingdot.domain.model.ext.id
import com.ts.connectingdot.helper.navigateTo
import com.ts.connectingdot.ui.Screens
import com.ts.connectingdot.ui.comp.UserCard

@Composable
fun NewChatScreen(
    navController: NavController,
    viewModel: NewChatViewModel,
) {


    LaunchedEffect(key1 = Unit) {
        viewModel.start()
    }

    TitleBarScaffold(
        title = "New Chat",
        navigateUp = { navController.navigateUp() }
    ) { paddingValues ->


        viewModel.userListTask.whenLoaded { userList ->
            LazyColumn (modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                items(userList){ user ->
                    UserCard(
                        user = user,
                        onClick = {
                            viewModel.onUserSelected(
                                otherUserId = user.id(),
                                onChannelReady = { channelId ->
                                    navController.navigateTo(
                                        Screens.Chat(channelId = channelId),
                                        Screens.NewChat)
                                }
                            )
                        })
                }
            }
        }



    }
}