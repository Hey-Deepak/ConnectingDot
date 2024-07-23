package com.ts.connectingdot.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.streamliners.pickers.media.PickedMedia
import com.ts.connectingdot.data.remote.FirestoreCollection.channelColl
import com.ts.connectingdot.domain.model.Channel
import com.ts.connectingdot.domain.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChannelRepo {

    suspend fun getOneToOneChannel(
        currentUserId: String,
        otherUserId: String
    ): Channel? {

        return Firebase.firestore
            .channelColl()
            .whereEqualTo(Channel::type.name, Channel.Type.OneToOne)
            .whereArrayContainsAny(Channel::members.name, listOf(currentUserId, otherUserId))
            .get()
            .await()
            .toObjects(Channel::class.java)
            .filter {
                it.members == listOf(
                    currentUserId,
                    otherUserId
                ) || it.members == listOf(otherUserId, currentUserId)
            }
            .firstOrNull()

    }

    suspend fun createOneToOneChannel(currentUserId: String, otherUserId: String): String {
        val collRef = Firebase.firestore.channelColl()
        val id = collRef.document().id

        collRef.document(id)
            .set(
                Channel(
                    id = null,
                    imageUrl = null,
                    type = Channel.Type.OneToOne,
                    description = null,
                    name = "One2One",
                    members = listOf(currentUserId, otherUserId),
                    messages = emptyList()
                )
            ).await()

        return id
    }

    suspend fun createGroupChannel(
        currentUserId: String,
        name: String,
        description: String?,
        groupImageUrl: String?,
        members: List<String>,
        ): String {
        val collRef = Firebase.firestore.channelColl()
        val id = collRef.document().id

        collRef.document(id)
            .set(
                Channel(
                    id = null,
                    imageUrl = groupImageUrl,
                    type = Channel.Type.Group,
                    description = description,
                    name = name,
                    members = members + currentUserId,
                    messages = emptyList()
                )
            ).await()

        return id
    }

    suspend fun getAllChannelsOf(userId: String): List<Channel> {

        return Firebase.firestore.channelColl()
            .whereArrayContains(Channel::members.name, userId)
            .get()
            .await()
            .toObjects(Channel::class.java)

    }

    suspend fun getChannel(channelId: String): Channel {

        return Firebase.firestore
            .channelColl()
            .document(channelId)
            .get()
            .await()
            .toObject(Channel::class.java)
            ?: error("Channel not found and Id is $channelId")

    }

    suspend fun subscribeToChannel(channelId: String): Flow<Channel> {
        return callbackFlow {
            val registration = Firebase.firestore
                .channelColl()
                .document(channelId)
                .addSnapshotListener { snapshot, error ->
                    error?.let { throw error }
                    val channel = snapshot?.toObject(Channel::class.java)
                    channel?.let {
                        CoroutineScope(coroutineContext).launch {
                            send(it)
                        }
                    }
                }
            awaitClose{
                registration.remove()
            }
        }

    }
    suspend fun sendMessage(channelId: String, message: Message) {
        Firebase.firestore
            .channelColl()
            .document(channelId)
            .update(Channel::messages.name, FieldValue.arrayUnion(message))
            .await()
    }

}


