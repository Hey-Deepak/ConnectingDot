package com.ts.connectingdot.feature.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.base.taskState.value
import com.ts.connectingdot.data.LocalRepo
import com.ts.connectingdot.data.remote.ChannelRepo
import com.ts.connectingdot.data.remote.UserRepo
import com.ts.connectingdot.domain.model.Channel
import com.ts.connectingdot.domain.model.ext.id
import com.ts.connectingdot.domain.model.ext.profileImageUrl
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class HomeViewModel(
    private val localRepo: LocalRepo,
    private val channelRepo: ChannelRepo,
    private val userRepo: UserRepo
) : BaseViewModel() {


    val channelsState = taskStateOf<List<Channel>>()
    val otherUserIdState = mutableStateOf("")

    fun start() {
        execute {
            val userId = localRepo.getLoggedInUser().id()

            val users = userRepo.getAllUsers()
            val channels = channelRepo.getAllChannelsOf(userId)
                .map { channel ->
                    if (channel.type == Channel.Type.OneToOne) {
                        val otherUserId =
                            channel.members.find { it != userId } ?: error("OtherUserId not found")
                        val otherUser =
                            users.find { it.id() == otherUserId } ?: error("User with Id not found")

                        channel.copy(
                            name = otherUser.name,
                            imageUrl = otherUser.profileImageUrl()
                        )
                    } else {
                        channel
                    }
                }

            channelsState.update(channels)

            subscribeForGroupNotification()
        }
    }

    private fun subscribeForGroupNotification() {
        execute(false) {
            channelsState.value()
                .filter {
                    it.type == Channel.Type.Group
                }.forEach { channel ->
                    Firebase.messaging.subscribeToTopic(
                        channel.id()
                    ).await()
                }
        }
    }

    fun getOtherUserId(channel: Channel){
        runBlocking {
            val currentUserId = localRepo.getLoggedInUser().id()
            val otherUserId = channel.members.find { it != currentUserId }
            otherUserIdState.value = otherUserId ?: error("Other User Id Not found in HVM")
        }
    }

}