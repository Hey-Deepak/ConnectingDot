package com.ts.connectingdot.domain.usecase

import com.ts.connectingdot.data.remote.OtherRepo
import com.ts.connectingdot.data.remote.UserRepo
import com.ts.connectingdot.helper.fcm.AndroidPayload
import com.ts.connectingdot.helper.fcm.FcmMessage
import com.ts.connectingdot.helper.fcm.FcmPayload
import com.ts.connectingdot.helper.fcm.FcmSender
import com.ts.connectingdot.helper.fcm.NotificationPayload

class NewMessageNotifier(
    private val otherRepo: OtherRepo,
    private val fcmSender: FcmSender,
    private val userRepo: UserRepo
) {

    suspend fun notifySingleUser(
        senderName: String,
        userId: String,
        message: String
    ){
        val token = userRepo.getUserById(userId).fcmToken ?: return
        val payload = FcmPayload(
            FcmMessage.forToken(
                token = token,
                notification = NotificationPayload(
                    title = "New Message",
                    body = "$senderName : $message"
                ),
                android = AndroidPayload(
                    priority = "high"
                ),
                data = null
            )
        )
        sendNotification(payload)
    }

    suspend fun notifyMultipleUserUsingTopic(
        senderName: String,
        topic: String,
        message: String
    ){

        val payload = FcmPayload(
            FcmMessage.forTopic(
                topic = topic,
                notification = NotificationPayload(
                    title = "New Message",
                    body = "$senderName : $message"
                ),
                android = AndroidPayload(
                    priority = "high"
                ),
                data = null
            )
        )
        sendNotification(payload)
    }

    private suspend fun sendNotification(payload: FcmPayload) {

        val svcAcJson = otherRepo.getServiceAccountJson()
        fcmSender.send(
            fcmPayload = payload,
            serviceAccountJson = svcAcJson
        )
    }

}