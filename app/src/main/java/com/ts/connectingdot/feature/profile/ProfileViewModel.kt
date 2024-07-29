package com.ts.connectingdot.feature.profile

import androidx.compose.runtime.mutableStateOf
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf
import com.ts.connectingdot.data.remote.UserRepo
import com.ts.connectingdot.domain.model.User

class ProfileViewModel(
    private val userRepo: UserRepo
) : BaseViewModel() {

    val otherUserNameState = mutableStateOf("A")
    val otherUserState = taskStateOf<User>()

    fun start(userId: String) {

        execute(true) {

            otherUserNameState.value = userRepo.getUserById(userId).name
            otherUserState.load {
                userRepo.getUserById(userId)
            }

        }
    }
}