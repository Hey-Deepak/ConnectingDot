package com.ts.connectingdot.data

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreCollection {

    fun FirebaseFirestore.userColl()  = collection("users")

}