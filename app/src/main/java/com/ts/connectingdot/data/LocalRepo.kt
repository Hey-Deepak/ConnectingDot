package com.ts.connectingdot.data

import com.streamliners.base.exception.failure
import com.ts.connectingdot.domain.model.User
import com.ts.connectingdot.helper.DataStoreUtil

class LocalRepo(
    val dataStoreUtil: DataStoreUtil
) {

    companion object {
        private const val KEY_USER = "user"
        private const val KEY_IS_SUBSCRIBE_FOR_GROUP_NOTIFICATION = "IS_SUBSCRIBE_FOR_GROUP_NOTIFICATION"
    }

    suspend fun upsertCurrentUser(user: User){
        dataStoreUtil.setData(KEY_USER, user)
    }

    suspend fun getLoggedInUser(): User {
        return getLoggedInUserNullable()?: error("User not found in Local Database 1")
    }
    suspend fun getLoggedInUserNullable(): User? {
        return dataStoreUtil.getData<User>(KEY_USER)
    }

    suspend fun isLoggedIn() = getLoggedInUserNullable() != null



}