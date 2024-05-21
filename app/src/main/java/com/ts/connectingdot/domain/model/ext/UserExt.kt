package com.ts.connectingdot.domain.model.ext

import com.ts.connectingdot.domain.model.User
import com.ts.connectingdot.helper.userInitialBasedProfileProfile

fun User.id(): String {
    return id ?: error("Id not Found")
}

fun User.profileImageUrl(): String{

    return profileImageUrl ?: userInitialBasedProfileProfile(name)

}