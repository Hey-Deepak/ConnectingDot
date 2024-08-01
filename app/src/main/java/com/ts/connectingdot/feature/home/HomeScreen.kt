package com.ts.connectingdot.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.comp.CenterText
import com.streamliners.helpers.NotificationHelper
import com.ts.connectingdot.domain.model.Channel
import com.ts.connectingdot.domain.model.ext.id
import com.ts.connectingdot.feature.home.comp.ChannelCard
import com.ts.connectingdot.ui.Screens
import com.ts.connectingdot.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.start()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Home") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screens.EditProfile().route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "EditProfile",
                            // TODO: Avoid using hardcode color
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                FloatingActionButton(onClick = {
                    navController.navigate(Screens.NewGroupChat.route)
                }) {
                    Icon(imageVector = Icons.Default.Group, contentDescription = "New Group Chat")
                }

                FloatingActionButton(onClick = {
                    navController.navigate(Screens.NewChat.route)
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "New OneToOne Chat")
                }
            }

        }
    ) { paddingValues ->

        viewModel.channelsState.whenLoaded { channels ->

            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (channels.isEmpty()) {
                    item {
                        CenterText(text = "No Chat's Found!")
                    }
                } else {
                    items(channels) { channel ->
                        ChannelCard(
                            channel = channel,
                            onChannelCardClick = {
                                navController.navigate(Screens.Chat(channelId = channel.id()).route)
                            },

                            onImageClick = {
                                viewModel.getOtherUserId(channel)
                                if (channel.type == Channel.Type.OneToOne) {
                                    navController.navigate(Screens.Profile(userId = viewModel.otherUserIdState.value).route)
                                } else {
                                    navController.navigate(Screens.GroupInfo(channelId = channel.id()).route)
                                }
                            }
                        )

                    }

                }

            }


        }


    }

    NotificationHelper.PermissionsSetup()

}