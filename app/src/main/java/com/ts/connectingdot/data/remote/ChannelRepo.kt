package com.ts.connectingdot.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.ts.connectingdot.data.remote.FirestoreCollection.channelColl
import com.ts.connectingdot.domain.model.Channel
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
                    it.members == listOf(currentUserId, otherUserId) || it.members == listOf(otherUserId, currentUserId)
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
                         imageUrl= null,
                         type = Channel.Type.OneToOne,
                         description = null,
                         name = "One2One",
                         members = listOf(currentUserId, otherUserId),
                         messages = emptyList()
                    )
               ).await()

          return id
     }

     suspend fun getAllChannelsOf(userId: String):List<Channel> {

          return Firebase.firestore.channelColl()
               .whereEqualTo(Channel::type.name, Channel.Type.OneToOne)
               .whereArrayContains(Channel::members.name, listOf(userId))
               .get()
               .await()
               .toObjects(Channel::class.java)

     }


}