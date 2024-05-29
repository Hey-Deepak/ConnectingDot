package com.ts.connectingdot.feature.chat

import android.icu.util.TimeZone.SystemTimeZoneType
import androidx.core.net.toUri
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.base.taskState.update
import com.streamliners.base.taskState.value
import com.streamliners.utils.DateTimeUtils
import com.ts.connectingdot.data.LocalRepo
import com.ts.connectingdot.data.remote.ChannelRepo
import com.ts.connectingdot.data.remote.StorageRepo
import com.ts.connectingdot.data.remote.UserRepo
import com.ts.connectingdot.domain.model.Channel
import com.ts.connectingdot.domain.model.Message
import com.ts.connectingdot.domain.model.User
import com.ts.connectingdot.domain.model.ext.id
import com.ts.connectingdot.domain.usecase.NewMessageNotifier
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatViewModel(
    private val channelRepo: ChannelRepo,
    private val localRepo: LocalRepo,
    private val storageRepo: StorageRepo,
    private val newMessageNotifier: NewMessageNotifier
): BaseViewModel() {

    sealed class ChatListItem{
        class ReceivedMessage(
            val time: String,
            val message: Message
        ): ChatListItem()

        class Date(val date: String): ChatListItem()

        class SendMessage(
            val time: String,
            val message: Message
        ): ChatListItem()
    }

    class Data(
        val channel: Channel,
        val user: User,
        val chatListItems: List<ChatListItem>
    )

    val data = taskStateOf<Data>()

    fun start(
        channelId: String
    ){

        execute {
            val user = localRepo.getLoggedInUser()
            launch {
                channelRepo.subscribeToChannel(channelId).collectLatest {

                    val channel = channelRepo.getChannel(channelId)
                    data.update(
                        Data(
                            channel,
                            user,
                            createChatListItem(channel, user.id())
                        )
                    )
                }
            }

        }

    }

    fun createChatListItem(channel: Channel, currentUserId: String): List<ChatListItem> {
        val chatItems = mutableListOf<ChatListItem>()
        var prevDateString = ""
        for (message in channel.messages) {
            val dateString = DateTimeUtils.formatTime(
                DateTimeUtils.Format.DATE_MONTH_YEAR_1,
                message.time.toDate().time,

            )

            if (prevDateString != dateString) {
                chatItems.add(ChatListItem.Date(dateString))
                prevDateString = dateString
            }

            val chatListItem = if (message.sender == currentUserId) {
                ChatListItem.SendMessage(message.time.toString(), message)
            } else {
                ChatListItem.ReceivedMessage(message.time.toString(), message)

            }
            chatItems.add(chatListItem)
        }

        return chatItems
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
            newMessageNotifier.notify()
            onSuccess()
        }
    }

    fun sendImage(uri: String){


        execute {
            val email = localRepo.getLoggedInUser().email
            val timestamp = System.currentTimeMillis()
            // TODO: Use the exact file extension
            val imageUrl = storageRepo.uploadFile("media/$timestamp-$email", uri.toUri())
            val message = Message(
                sender = data.value().user.id(),
                message = "",
                mediaUrl = imageUrl,
            )

            channelRepo.sendMessage(data.value().channel.id(), message)
        }


    }
}