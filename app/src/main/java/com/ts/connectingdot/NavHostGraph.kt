package com.ts.connectingdot

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ts.connectingdot.feature.login.LoginScreen
import com.ts.connectingdot.feature.splash.SplashScreen


@Composable
fun NavHostGraph(
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
                navController
            )
        }

        composable(
            route = Screens.Login.route,
        ){
            LoginScreen()
        }

        composable(
            route = Screens.Profile.route,
        ){
            LoginScreen()
        }


    }

}