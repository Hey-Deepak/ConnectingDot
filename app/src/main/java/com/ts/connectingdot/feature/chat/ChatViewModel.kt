package com.ts.connectingdot.feature.chat

import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.base.taskState.value
import com.ts.connectingdot.data.LocalRepo
import com.ts.connectingdot.data.remote.ChannelRepo
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

    class Data(
        val channel: Channel,
        val user: User
    )

    val data = taskStateOf<Data>()

    fun start(
        channelId: String
    ){

        execute {
            val user = localRepo.getLoggedInUser()
            launch {
                channelRepo.subscribeToChannel(channelId).collectLatest {
                    data.update(
                        Data(
                            channelRepo.getChannel(channelId),
                            user
                        )
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
            sender = data.value().user.id(),
            message = messageStr,
            mediaUrl = null,
        )
        execute {
            channelRepo.sendMessage(data.value().channel.id(), message)
            onSuccess()
        }
    }
}