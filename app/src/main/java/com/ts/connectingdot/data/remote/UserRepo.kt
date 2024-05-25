package com.ts.connectingdot.data.remote

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ts.connectingdot.data.remote.FirestoreCollection.userColl
import com.ts.connectingdot.domain.model.Channel
import com.ts.connectingdot.domain.model.User
import kotlinx.coroutines.tasks.await

class UserRepo {

    suspend fun saveUser(user: User): User {

        val collRef = Firebase.firestore.userColl()
        val id = collRef.document().id

        collRef.document(id).set(user).await()

        return user.copy(id = id)
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

    suspend fun getAllUsers(): List<User> {
        return Firebase.firestore
            .userColl()
            .get()
            .await()
            .toObjects(User::class.java)
    }

}