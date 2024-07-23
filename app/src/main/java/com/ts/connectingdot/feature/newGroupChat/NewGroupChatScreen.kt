package com.ts.connectingdot.feature.newGroupChat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.android.comp.appBar.TitleBar
import com.streamliners.compose.comp.textInput.config.InputConfig
import com.streamliners.compose.comp.textInput.config.text
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.compose.comp.textInput.state.ifValidInput
import com.streamliners.compose.comp.textInput.state.nullableValue
import com.streamliners.compose.comp.textInput.state.value
import com.streamliners.pickers.media.PickedMedia
import com.ts.connectingdot.feature.newGroupChat.comp.GroupInfoInput
import com.ts.connectingdot.feature.newGroupChat.comp.MembersInput
import com.ts.connectingdot.helper.navigateTo
import com.ts.connectingdot.ui.Screens

@Composable
fun NewGroupChatScreen(
    navController: NavController,
    viewModel: NewGroupChatViewModel,
) {


    LaunchedEffect(key1 = Unit) {
        viewModel.start()
    }
    val image = remember {
        mutableStateOf<PickedMedia?>(null)
    }

    val nameInput = remember {
        mutableStateOf(
            TextInputState(
                label = "Group Name",
                inputConfig = InputConfig.text {
                    minLength = 5
                    maxLength = 30
                }
            )
        )
    }

    val descriptionInput = remember {
        mutableStateOf(
            TextInputState(
                label = "Group Description",
                inputConfig = InputConfig.text {
                    optional = true
                }
            )
        )
    }

    val members = remember {
        mutableStateListOf<String>()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TitleBar(
                title = "New Group Chat",
                navigateUp = { navController.navigateUp() }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    nameInput.ifValidInput { name ->
                        viewModel.createGroupChannel(
                            name, descriptionInput.nullableValue(), image.value, members
                        ){ channelId ->
                            navController.navigateTo(
                                Screens.Chat(channelId),
                                Screens.NewGroupChat)
                        }
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Groups, contentDescription = "Create")

                Spacer(modifier = Modifier.size(13.dp))

                Text("Create")
            }
        }
    ) { paddingValues ->





        // TODO: Avoid hiding entire screen while the membersList loads

        viewModel.userListTask.whenLoaded { userList ->
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    bottom = 70.dp
                )
            ) {
                item {
                    GroupInfoInput(nameInput, descriptionInput, image)
                }
                MembersInput(userList, members)
            }
        }


    }
}


