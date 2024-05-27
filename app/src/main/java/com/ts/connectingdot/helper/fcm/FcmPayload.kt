package com.ts.connectingdot.helper.fcm

data class FcmPayload(
    val message: FcmMessage
)

class FcmMessage private constructor(
    val topic: String?,
    val token: String?,
    val notification: NotificationPayload?,
    val data: Map<String, String>?,
    val android: AndroidPayload?
) {

    companion object {
        fun forTopic(
            topic: String,
            notification: NotificationPayload?,
            android: AndroidPayload?,
            data: Map<String, String>?) = FcmPayload(
                message = FcmMessage(
                    topic,
                    token = null,
                    notification,
                    data,
                    android
                )
            )

        fun forToken(
            token: String,
            notification: NotificationPayload?,
            android: AndroidPayload?,
            data: Map<String, String>?) = FcmPayload(
                message = FcmMessage(
                    topic = null,
                    token = token,
                    notification,
                    data,
                    android
                )
            )
    }

}

data class NotificationPayload(
    val title: String?,
    val body: String?
)

class AndroidPayload(
    val priority: String?
)