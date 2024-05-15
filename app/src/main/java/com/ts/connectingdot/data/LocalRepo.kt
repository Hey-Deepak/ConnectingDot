package com.ts.connectingdot.data

import com.ts.connectingdot.helper.DataStoreUtil

class LocalRepo(
    val dataStoreUtil: DataStoreUtil
) {

    suspend fun onLoggedIn(){
        dataStoreUtil.setData("isLoggedIn", true)
    }

    suspend fun isLoggedIn(): Boolean {
        return dataStoreUtil.getData<Boolean>("isLoggedIn")?: false
    }

}