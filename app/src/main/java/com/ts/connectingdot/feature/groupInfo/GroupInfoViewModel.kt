package com.ts.connectingdot.feature.groupInfo

import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.base.taskState.value
import com.ts.connectingdot.data.remote.ChannelRepo
import com.ts.connectingdot.data.remote.UserRepo
import com.ts.connectingdot.domain.model.Channel
import com.ts.connectingdot.domain.model.User

class GroupInfoViewModel(
    private val channelRepo: ChannelRepo,
    private val userRepo: UserRepo
): BaseViewModel() {

    data class Data(
        val channel: Channel,
        val members: List<User>
    )

    val dataState = taskStateOf<Data>()

    fun start(channelId: String){
        execute {
            val channel = channelRepo.getChannel(channelId)
            val members = mutableListOf<User>()
            channel.members.forEach {
                members.add(userRepo.getUserById(it))
            }
            val data = Data(
                channel = channel,
                members = members
            )
            dataState.update(data)
        }
    }
}

