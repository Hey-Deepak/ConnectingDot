package com.ts.connectingdot.feature.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.streamliners.base.taskState.comp.whenLoaded
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.ts.connectingdot.feature.editProfile.comp.ProfileImage
import com.ts.connectingdot.helper.userInitialBasedProfileProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: String,
    navController: NavHostController,
    viewModel: ProfileViewModel
) {

    TitleBarScaffold(
        title = viewModel.otherUserNameState.value,
        navigateUp = {navController.navigateUp()}

    ) { paddingValues ->

        LaunchedEffect(key1 = Unit) {
            viewModel.start(userId)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TODO: Handle Null profile here with default Pic

                viewModel.otherUserState.whenLoaded {
                    ProfileImage(
                        modifier = Modifier.size(150.dp),
                        data = it.profileImageUrl
                            ?: userInitialBasedProfileProfile(it.name)
                    ) {}

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(text = "Bio",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = it.bio)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(text = "Email-Id",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = it.email)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(text = "Date of Birth",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        // TODO: DOB can't me Empty right
                        Text(text = it.dob ?: "N/A")
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                }

            }


        }
    }
}