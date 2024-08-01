package com.ts.connectingdot.feature.groupInfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.streamliners.base.BaseViewModel
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.ts.connectingdot.domain.model.ext.id
import com.ts.connectingdot.feature.editProfile.comp.ProfileImage
import com.ts.connectingdot.feature.newGroupChat.comp.GroupInfoInput
import com.ts.connectingdot.feature.newGroupChat.comp.MembersInput
import com.ts.connectingdot.helper.userInitialBasedProfileProfile
import com.ts.connectingdot.ui.comp.UserCard
import com.ts.connectingdot.ui.theme.Primary

@Composable
fun GroupInfoScreen(
    channelId: String,
    navController: NavHostController,
    viewModel: GroupInfoViewModel
) {
    TitleBarScaffold(
        title = "Group-Info",
        navigateUp = { navController.navigateUp() }

    ) { paddingValues ->

        LaunchedEffect(key1 = Unit) {
            viewModel.start(channelId)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // TODO: Avoid hiding entire screen while the membersList loads

            viewModel.dataState.whenLoaded { data ->

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // TODO: Handle Null profile here with default Pic
                    ProfileImage(
                        modifier = Modifier.size(150.dp),
                        data = data.channel.imageUrl
                            ?: userInitialBasedProfileProfile("G")
                    ) {}
                }

                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(
                        bottom = 70.dp
                    )
                ) {
                    
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(text = "Description",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = data.channel.description ?: "N/A")
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    item {
                        Text(
                            modifier = Modifier
                                .padding(start = 16.dp),
                            text = "Members",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    items(data.members) { user ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .padding(horizontal = 16.dp)
                        ) {
                            UserCard(user = user)
                        }

                    }
                }
            }
        }
    }
}