package com.ts.connectingdot.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.streamliners.base.ext.koinBaseViewModel
import com.streamliners.pickers.date.showDatePickerDialog
import com.ts.connectingdot.MainActivity
import com.ts.connectingdot.feature.login.LoginScreen
import com.ts.connectingdot.feature.editProfile.EditProfileScreen
import com.ts.connectingdot.feature.home.HomeScreen
import com.ts.connectingdot.feature.splash.SplashScreen
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel


@Composable
fun MainActivity.NavHostGraph(
    navController: NavHostController
) {



    NavHost(
        startDestination = Screens.Login.route,
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
            route = Screens.Login.route,
            arguments = listOf(
                navArgument("email"){
                    type = NavType.StringType
                }
            )
        ){
            LoginScreen(
                navController = navController,
                koinViewModel()
            )
        }


        // Edit Profile Screen
        composable(
            route = Screens.EditProfile.format(),
        ){
            val email = it.arguments?.getString("email")?: error("Email Not Found")
            EditProfileScreen(
                navController,
                koinBaseViewModel(),
                email,
                showDatePicker = ::showDatePickerDialog
            )
        }

        composable(
            route = Screens.Home.route
        ){
            HomeScreen()
        }

    }
}