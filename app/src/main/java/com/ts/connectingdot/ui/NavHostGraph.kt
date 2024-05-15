package com.ts.connectingdot.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ts.connectingdot.feature.login.LoginScreen
import com.ts.connectingdot.feature.editProfile.EditProfileScreen
import com.ts.connectingdot.feature.home.HomeScreen
import com.ts.connectingdot.feature.splash.SplashScreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun NavHostGraph(
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

        composable(
            route = Screens.EditProfile.format(),
        ){

            val email = it.arguments?.getString("email")?: error("Email Not Found")

            EditProfileScreen(
                navController,
                koinViewModel(),
                email
            )
        }

        composable(
            route = Screens.Home.route
        ){
            HomeScreen()
        }


    }

}