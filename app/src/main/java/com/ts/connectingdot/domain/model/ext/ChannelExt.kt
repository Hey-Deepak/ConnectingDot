package com.ts.connectingdot.domain.model.ext

import com.ts.connectingdot.domain.model.Channel

fun Channel.id(): String {
    return id ?: error("Channel id not found")
}