package com.ts.connectingdot.domain.model

import com.google.firebase.Timestamp

data class Message(
    val time: Timestamp,
    // UserId of the sender
    val sender: String,
    val message: String,
    val mediaUrl: String?
){

    constructor() : this(Timestamp.now(), "", "", null)
}
