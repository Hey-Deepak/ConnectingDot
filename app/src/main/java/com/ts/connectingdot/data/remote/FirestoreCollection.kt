package com.ts.connectingdot.data.remote

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreCollection {

    fun FirebaseFirestore.userColl()  = collection("users")
    fun FirebaseFirestore.channelColl()  = collection("channel")

}