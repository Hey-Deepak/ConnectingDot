package com.ts.connectingdot.data

import com.ts.connectingdot.domain.model.User
import com.ts.connectingdot.helper.DataStoreUtil

class LocalRepo(
    val dataStoreUtil: DataStoreUtil
) {

    suspend fun onLoggedIn(user: User){
        dataStoreUtil.setData("user", user)
    }

    suspend fun getLoggedInUser(): User {
        return getLoggedInUserNullable()?: error("User not found in Local Database")
    }
    suspend fun getLoggedInUserNullable(): User? {
        return dataStoreUtil.getData<User>("user")?: error("User not found in Local Database")
    }

    suspend fun isLoggedIn() = getLoggedInUserNullable() != null

}