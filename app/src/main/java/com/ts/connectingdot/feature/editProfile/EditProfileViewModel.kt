package com.ts.connectingdot.feature.editProfile

import androidx.compose.runtime.MutableState
import androidx.core.net.toUri
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.executeOnMain
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.pickers.media.PickedMedia
import com.ts.connectingdot.data.LocalRepo
import com.ts.connectingdot.data.remote.StorageRepo
import com.ts.connectingdot.data.remote.UserRepo
import com.ts.connectingdot.domain.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo,
    private val storageRepo: StorageRepo
) : BaseViewModel() {

    val saveProfileTask = taskStateOf<Unit>()

    fun saveUser(user: User, image: MutableState<PickedMedia?>, onSuccess: () -> Unit) {

        execute(showLoadingDialog = false) {

            saveProfileTask.load {

                val token = Firebase.messaging.token.await()

                var updatedUser = user.copy(
                    profileImageUrl = uploadProfileImage(user.email, image.value),
                    fcmToken = token
                )

                updatedUser = userRepo.saveUser(user = updatedUser)
                localRepo.upsertCurrentUser(updatedUser)
                executeOnMain {
                    onSuccess()
                }
            }

        }
    }

    private suspend fun uploadProfileImage(email: String, image: PickedMedia?): String? {

        return image?.let {
            // TODO: upload image using userId
            // TODO: Use the exact file ext
            storageRepo.uploadFile("profileImages/$email.jpg", it.uri.toUri())
        } 

    }

}