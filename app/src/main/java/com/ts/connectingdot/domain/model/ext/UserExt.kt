package com.ts.connectingdot.domain.model.ext

import com.ts.connectingdot.domain.model.User

fun User.id(): String {
    return id ?: error("Id not Found")
}