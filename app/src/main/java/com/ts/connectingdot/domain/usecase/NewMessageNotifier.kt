package com.ts.connectingdot.domain.usecase

import com.ts.connectingdot.data.remote.OtherRepo
import com.ts.connectingdot.helper.fcm.AndroidPayload
import com.ts.connectingdot.helper.fcm.FcmMessage
import com.ts.connectingdot.helper.fcm.FcmPayload
import com.ts.connectingdot.helper.fcm.FcmSender
import com.ts.connectingdot.helper.fcm.NotificationPayload

class NewMessageNotifier(
    private val otherRepo: OtherRepo,
    private val fcmSender: FcmSender
) {

    suspend fun notify(){
        val svcAcJson = otherRepo.getServiceAccountJson()
        val payload = FcmPayload(
            FcmMessage.forTopic(
                topic = "general",
                notification = NotificationPayload(
                    title = "Sample Notification",
                    body = "Android Studio se aa Raha hai"
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