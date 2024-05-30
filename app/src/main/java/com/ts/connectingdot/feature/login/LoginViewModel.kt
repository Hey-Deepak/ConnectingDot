package com.ts.connectingdot.feature.login

import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.executeOnMain
import com.ts.connectingdot.data.LocalRepo
import com.ts.connectingdot.data.remote.UserRepo
import com.ts.connectingdot.domain.model.ext.id
import com.ts.connectingdot.helper.navigateTo
import com.ts.connectingdot.ui.Screens
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo
) : BaseViewModel() {

    fun onLoggedIn(email: String, navController: NavController) {

        execute {
            var user = userRepo.getUserWithEmail(email)

            // Subscribe to FCM Topic
            Firebase.messaging.subscribeToTopic("general").await()

            if (user != null) {

                // Update Fcm Token
                val token = Firebase.messaging.token.await()
                user = user.copy(fcmToken = token)
                userRepo.updateFcmToken(token, user.id())

                // Save in Local DB
                localRepo.upsertCurrentUser(user)

                // Navigate to Home
                executeOnMain {
                    navController.navigate(Screens.Home.route)
                }
            } else
                executeOnMain {
                    navController.navigateTo(Screens.EditProfile(email), Screens.Login)
                }

        }

    }

}