package com.ts.connectingdot.helper.fcm

import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import java.io.ByteArrayInputStream

class FcmSender (
    private val client: HttpClient
){

    val projectId = "connectingdot-be492"
    val endPoint = "https://fcm.googleapis.com/v1/projects/$projectId/messages:send"

    suspend fun send(fcmPayload: FcmPayload, serviceAccountJson: String){
        val token = getAccessToken(serviceAccountJson)
        client.post(endPoint){
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(
                Gson().toJson(fcmPayload)
            )
        }

    }

    private fun getAccessToken(
        serviceAccountJson: String
    ): String {

        val googleCredential = GoogleCredential
            .fromStream(
                ByteArrayInputStream(serviceAccountJson.toByteArray())
            )
            .createScoped(
                listOf("https://www.googleapis.com/auth/firebase.messaging")
            )
        googleCredential.refesh()
        return googleCredential.accessToken.tokenValue
    }

}