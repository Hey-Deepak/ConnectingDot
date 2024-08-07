package com.ts.connectingdot.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.streamliners.base.ext.koinBaseViewModel
import com.streamliners.pickers.date.showDatePickerDialog
import com.ts.connectingdot.MainActivity
import com.ts.connectingdot.feature.chat.ChatScreen
import com.ts.connectingdot.feature.login.LoginScreen
import com.ts.connectingdot.feature.editProfile.EditProfileScreen
import com.ts.connectingdot.feature.home.HomeScreen
import com.ts.connectingdot.feature.newChat.NewChatScreen
import com.ts.connectingdot.feature.newGroupChat.NewGroupChatScreen
import com.ts.connectingdot.feature.splash.SplashScreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun MainActivity.NavHostGraph(
    navController: NavHostController
) {

    NavHost(
        startDestination = Screens.Splash.route,
        navController = navController
    ){

        composable(
            route = Screens.Splash.route,
        ){
            SplashScreen(
                navController,
                koinViewModel()
            )
        }

        composable(
            route = Screens.Login.route
        ){
            LoginScreen(
                navController = navController,
                koinBaseViewModel()
            )
        }


        // Edit Profile Screen
        composable(
            route = Screens.EditProfile.format(),
            arguments = listOf(
                navArgument("email"){
                    type = NavType.StringType
                    nullable = true
                }
            )
        ){
            // TODO: Improve readability bu using enum for mode (Create / Edit)
            val email = it.arguments?.getString("email")
            EditProfileScreen(
                navController,
                koinBaseViewModel(),
                email,
                showDatePicker = ::showDatePickerDialog
            )
        }

        // Home Screen
        composable(
            route = Screens.Home.route
        ){
            HomeScreen(
                navController,
                koinBaseViewModel()
            )
        }

        // New Chat Screen
        composable(
            route = Screens.NewChat.route
        ){
            NewChatScreen(
                navController,
                koinBaseViewModel()
            )
        }
        // New Group Screen
        composable(
            route = Screens.NewGroupChat.route
        ){
            NewGroupChatScreen(
                navController,
                koinBaseViewModel()
            )
        }

        // Chat Screen
        composable(
            route = Screens.Chat.format(),
            arguments = listOf(
                navArgument("channelId"){
                    type = NavType.StringType
                }
            )
        ){
            val channelId = it.arguments?.getString("channelId")?: error("ChannelId Arg Not Found")
            ChatScreen(
                channelId = channelId,
                navController = navController,
                koinBaseViewModel())
        }

    }
}