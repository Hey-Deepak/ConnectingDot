package com.ts.connectingdot.feature.chat

import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.base.taskState.value
import com.ts.connectingdot.data.LocalRepo
import com.ts.connectingdot.data.remote.ChannelRepo
import com.ts.connectingdot.data.remote.UserRepo
import com.ts.connectingdot.domain.model.Channel
import com.ts.connectingdot.domain.model.Message
import com.ts.connectingdot.domain.model.User
import com.ts.connectingdot.domain.model.ext.id
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatViewModel(
    private val channelRepo: ChannelRepo,
    private val localRepo: LocalRepo
): BaseViewModel() {


    val channel = taskStateOf<Channel>()
    lateinit var user: User

    fun start(
        channelId: String
    ){

        execute {
            user = localRepo.getLoggedInUser()
            launch {
                channelRepo.subscribeToChannel(channelId).collectLatest {
                    channel.update(
                        channelRepo.getChannel(channelId)
                    )
                }
            }

        }

    }

    fun sendMessage(
        messageStr: String,
        onSuccess: () -> Unit,
    ){
        val message = Message(
            sender = user.id(),
            message = messageStr,
            mediaUrl = null,
        )
        execute {
            channelRepo.sendMessage(channel.value().id(), message)
            onSuccess()
        }
    }
}