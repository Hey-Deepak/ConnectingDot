package com.ts.connectingdot.helper.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.streamliners.helpers.NotificationHelper
import com.ts.connectingdot.MainActivity

class ChatAppFCMService : FirebaseMessagingService (){

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

    private fun showNotification(title: String?, body: String?, context: ChatAppFCMService = this) {

        NotificationHelper(this)
            .showNotification(
                title = title?: "No Title",
                body = body?: "No Body",
                pendingIntentActivity = MainActivity::class.java
            )

    }
}