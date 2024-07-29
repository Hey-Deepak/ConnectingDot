package com.ts.connectingdot.feature.profile

import androidx.compose.runtime.mutableStateOf
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.ts.connectingdot.data.remote.UserRepo
import com.ts.connectingdot.domain.model.User

class ProfileViewModel(
    private val userRepo: UserRepo
) : BaseViewModel() {

    val otherUserNameState = mutableStateOf("A")
    val otherUserState = mutableStateOf(User())

    fun start(userId: String) {

        execute(true) {

            otherUserNameState.value = userRepo.getUserById(userId).name
            otherUserState.value = userRepo.getUserById(userId)

        }
    }
}