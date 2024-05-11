package com.ts.connectingdot

sealed class Screens (
    val route: String
){

    data object Splash: Screens("splash")

    data object Login: Screens("login")

    data object Profile: Screens("profile")

    

}