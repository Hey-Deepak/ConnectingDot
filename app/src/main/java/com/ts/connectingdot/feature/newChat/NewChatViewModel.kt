package com.ts.connectingdot.feature.newChat

import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.executeOnMain
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf
import com.ts.connectingdot.data.LocalRepo
import com.ts.connectingdot.data.remote.ChannelRepo
import com.ts.connectingdot.data.remote.UserRepo
import com.ts.connectingdot.domain.model.User
import com.ts.connectingdot.domain.model.ext.id

class NewChatViewModel(
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo,
    private val channelRepo: ChannelRepo
) : BaseViewModel() {

    val userListTask = taskStateOf<List<User>>()
    fun start() {

        execute {

            val user = localRepo.getLoggedInUser()
            userListTask.load {
                userRepo.getAllUsers()
                    .filter { it.id() != user.id() }
            }
        }

    }

    fun onUserSelected(
        otherUserId: String,
        onChannelReady: (String) -> Unit
    ){
        execute {
            val currentUserId = localRepo.getLoggedInUser().id()
            val channel = channelRepo.getOneToOneChannel(currentUserId, otherUserId)
            val channelId = channel?.id() ?: channelRepo.createOneToOneChannel(currentUserId, otherUserId)
            executeOnMain {
                onChannelReady(channelId)
            }
        }
    }


}