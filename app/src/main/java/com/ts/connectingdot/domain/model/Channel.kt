package com.ts.connectingdot.domain.model

import com.google.firebase.firestore.DocumentId

data class Channel(
    @DocumentId
    val id: String?,
    val imageUrl: String?,
    val type: Type,
    val name: String,
    val description: String?,
    // List of UserId of members
    val members: List<String>,
    val messages: List<Message>
){
    constructor(): this(null, null, Type.OneToOne, "", null, emptyList(), emptyList())
    enum class Type{
        OneToOne, Group
    }
}
