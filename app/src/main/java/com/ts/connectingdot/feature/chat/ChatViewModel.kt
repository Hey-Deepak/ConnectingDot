package com.ts.connectingdot.feature.chat

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
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo,
    private val storageRepo: StorageRepo,
    private val newMessageNotifier: NewMessageNotifier
) : BaseViewModel() {

    sealed class ChatListItem {
        class ReceivedMessage(
            val time: String,
            val senderName: String?,
            val message: Message
        ) : ChatListItem()

        class Date(val date: String) : ChatListItem()

        class SendMessage(
            val time: String,
            val message: Message
        ) : ChatListItem()
    }

    class Data(
        val channel: Channel,
        val user: User,
        val chatListItems: List<ChatListItem>
    )

    val data = taskStateOf<Data>()

    fun start(
        channelId: String
    ) {

        execute {
            val user = localRepo.getLoggedInUser()
            launch {
                // TODO: Fetch list only if group channel
                val users = userRepo.getAllUsers()
                channelRepo.subscribeToChannel(channelId).collectLatest {

                    val channel = channelRepo.getChannel(channelId)
                    data.update(
                        Data(
                            channel = channel,
                            user = user,
                            chatListItems = createChatListItem(channel, user.id(), users)
                        )
                    )
                }
            }

        }

    }

    fun createChatListItem(
        channel: Channel,
        currentUserId: String,
        users: List<User>
    ): List<ChatListItem> {
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

                val name = if(channel.type == Channel.Type.Group){
                    users.find { it.id == message.sender }?.name
                        ?: error("User with id ${message.sender} not found!")
                } else null

                ChatListItem.ReceivedMessage(
                    time = DateTimeUtils.formatTime(
                        DateTimeUtils.Format.HOUR_MIN_12,
                        message.time.toDate().time
                    ),
                    message = message,
                    senderName = name
                )

            }
            chatItems.add(chatListItem)
        }

        return chatItems
    }

    fun sendMessage(
        messageStr: String,
        onSuccess: () -> Unit,
    ) {
        val message = Message(
            sender = data.value().user.id(),
            message = messageStr,
            mediaUrl = null,
        )
        execute {
            channelRepo.sendMessage(data.value().channel.id(), message)

            notifyOtherUser(messageStr)
            onSuccess()
        }
    }

    private fun notifyOtherUser(message: String) {
        val user = data.value().user
        val channel = data.value().channel
        if (channel.type == Channel.Type.OneToOne) {
            notifySingleUserUsingToken(channel, user, message)

        } else {
            notifyAllOtherUsersUsingTopic(channel, message)
        }


    }

    private fun notifyAllOtherUsersUsingTopic(channel: Channel, message: String) {
        // TODO: Send to all users except current user (silently receive)
        execute(false) {
            newMessageNotifier.notifyMultipleUserUsingTopic(
                senderName = data.value().user.name,
                topic = channel.id(),
                message = message
            )
        }

    }

    private fun notifySingleUserUsingToken(
        channel: Channel,
        user: User,
        message: String
    ) {
        val otherUserId =
            channel.members.find { it != user.id() } ?: error("OtherUserId not found")
        execute(false) {
            newMessageNotifier.notifySingleUser(
                senderName = data.value().user.name,
                userId = otherUserId,
                message = message
            )
        }
    }

    fun sendImage(uri: String) {


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
            notifyOtherUser("Sent an image")
        }


    }
}