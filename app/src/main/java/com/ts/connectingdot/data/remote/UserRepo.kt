package com.ts.connectingdot.data.remote

import android.provider.ContactsContract.CommonDataKinds.Email
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ts.connectingdot.data.FirestoreCollection.userColl
import com.ts.connectingdot.domain.model.User
import kotlinx.coroutines.tasks.await

class UserRepo {

    suspend fun saveUser(user: User){

        Firebase.firestore
            .userColl()
            .add(user)
            .await()

    }

    suspend fun getUserWithEmail(email: String):User?{
        return Firebase.firestore
            .userColl()
            .whereEqualTo(User::email.name, email)
            .get()
            .await()
            .toObjects(User::class.java)
            .firstOrNull()
    }

}