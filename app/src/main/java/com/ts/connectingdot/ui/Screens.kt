package com.ts.connectingdot.ui

sealed class Screens(
    val route: String
) {

    data object Splash : Screens("Splash")

    data object  Login : Screens("Login")

    class EditProfile(
        val email: String? = null
    ) : Screens("EditProfile?email$email") {

        companion object {
            fun format() = "EditProfile?email{email}"
        }

    }
    class Chat(
        val channelId: String
    ) : Screens("Chat?channelId$channelId") {

        companion object {
            fun format() = "Chat?channelId{channelId}"
        }

    }

    data object Home: Screens("Home")
    data object NewChat: Screens("NewChatScreen")
    data object NewGroupChat: Screens("NewGroupChatScreen")

}