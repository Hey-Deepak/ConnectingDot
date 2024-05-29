package com.ts.connectingdot.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.ts.connectingdot.data.remote.FirestoreCollection.otherColl
import com.ts.connectingdot.domain.model.Secret
import kotlinx.coroutines.tasks.await

class OtherRepo {

    suspend fun getServiceAccountJson(): String{
        return Firebase.firestore.otherColl()
            .document("secret")
            .get()
            .await()
            .toObject(Secret::class.java)
            ?.svcAc
            ?: error("svcAc not found on firestore")
    }

}