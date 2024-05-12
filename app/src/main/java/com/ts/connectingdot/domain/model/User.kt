package com.ts.connectingdot.domain.model

data class User(
    val name: String,
    val email: String,
    val bio: String,
    val gender: Gender
)
