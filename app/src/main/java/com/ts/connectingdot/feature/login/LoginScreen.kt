package com.ts.connectingdot.feature.login

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.ts.connectingdot.ui.Screens
import com.ts.connectingdot.ui.theme.Primary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Welcome to Chat App") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White
                )
            )
        }

    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {

            SignInWithGoogleButton(
                onSuccess = {
                    Log.e("FB", it.email.toString())

                    val email = it.email ?: error("Email Not Found")

                    viewModel.onLoggedIn(email, navController)

                },
                onError = {}
            )

        }

    }

}