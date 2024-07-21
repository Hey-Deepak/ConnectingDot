package com.ts.connectingdot.data.remote

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.google.protobuf.DescriptorProtos.FieldOptions
import com.ts.connectingdot.data.remote.FirestoreCollection.userColl
import com.ts.connectingdot.domain.model.Channel
import com.ts.connectingdot.domain.model.User
import com.ts.connectingdot.domain.model.ext.id
import kotlinx.coroutines.tasks.await

class UserRepo {

    suspend fun saveUser(user: User): User {

        val collRef = Firebase.firestore.userColl()

        try {
            if (user.id().isNotEmpty()){
                collRef.document(user.id()).set(user).await()
                return user.copy(id = user.id())
            } else {
                val id = getUserId(collRef, user)
                return user.copy(id = id)
            }
        } catch (e: Exception){
            val id = getUserId(collRef, user)
            Log.e("UserRepo", e.message.toString())
            return user.copy(id = id)
        }


    }

    private suspend fun getUserId(
        collRef: CollectionReference,
        user: User
    ): String {
        val id = collRef.document().id
        collRef.document(id).set(user).await()
        return id
    }

    suspend fun getUserWithEmail(email: String): User? {
        return Firebase.firestore
            .userColl()
            .whereEqualTo(User::email.name, email)
            .get()
            .await()
            .toObjects(User::class.java)
            .firstOrNull()
    }
    suspend fun getUserById(userId: String): User {
        return Firebase.firestore
            .userColl()
            .document(userId)
            .get()
            .await()
            .toObject(User::class.java)
            ?: error("No user found with Id = $userId")
    }

    suspend fun getAllUsers(): List<User> {
        return Firebase.firestore
            .userColl()
            .get()
            .await()
            .toObjects(User::class.java)
    }

    suspend fun updateFcmToken(token: String, userId: String){
        Firebase.firestore.userColl()
            .document(userId)
            .update(User::fcmToken.name, token)
            .await()

    }
}