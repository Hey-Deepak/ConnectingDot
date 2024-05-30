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

    suspend fun notify(
        senderName: String,
        userId: String,
        message: String
    ){
        val token = userRepo.getUserById(userId).fcmToken ?: return
        val svcAcJson = otherRepo.getServiceAccountJson()

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

        fcmSender.send(
            fcmPayload = payload,
            serviceAccountJson = svcAcJson
        )
    }

}