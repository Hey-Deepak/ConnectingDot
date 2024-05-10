package com.ts.connectingdot

sealed class Screens (
    route: String
){

    data object Splash: Screens("splash")

    data object Login: Screens("login")

    data object Profile: Screens("profile")

    

}