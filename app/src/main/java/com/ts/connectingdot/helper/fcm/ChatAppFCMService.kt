package com.ts.connectingdot.helper.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.streamliners.base.exception.defaultExecuteHandlingError
import com.streamliners.helpers.NotificationHelper
import com.ts.connectingdot.BuildConfig
import com.ts.connectingdot.MainActivity
import com.ts.connectingdot.data.LocalRepo
import com.ts.connectingdot.data.remote.UserRepo
import com.ts.connectingdot.domain.model.ext.id
import org.koin.android.ext.android.inject

class ChatAppFCMService : FirebaseMessagingService (){

    private val userRepo: UserRepo by inject()
    private val localRepo: LocalRepo by inject()

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.notification != null) {
            val title = message.notification!!.title
            val body = message.notification!!.body
            showNotification(title, body, this)
        } else {
            return
        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        //send token to server
        defaultExecuteHandlingError(
            lambda = {
                if (localRepo.isLoggedIn()){
                    val user = localRepo.getLoggedInUser().copy(
                        fcmToken = token
                    )
                    localRepo.upsertCurrentUser(user)
                    userRepo.updateFcmToken(token, user.id())
                }
            },
            buildType = BuildConfig.BUILD_TYPE
        )

    }

    private fun showNotification(title: String?, body: String?, context: ChatAppFCMService = this) {

        NotificationHelper(this)
            .showNotification(
                title = title?: "No Title",
                body = body?: "No Body",
                pendingIntentActivity = MainActivity::class.java
            )

    }
}