package com.ts.connectingdot

sealed class Screens(
    val route: String
) {

    data object Splash : Screens("Splash")

    data object Login : Screens("Login")

    class EditProfile(
        val email: String
    ) : Screens("EditProfile?email$email") {

        companion object {
            fun format() = "EditProfile?email{email}"
        }

    }


}