package com.ts.connectingdot.feature.editProfile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.executeOnMain
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf
import com.ts.connectingdot.data.LocalRepo
import com.ts.connectingdot.data.remote.StorageRepo
import com.ts.connectingdot.data.remote.UserRepo
import com.ts.connectingdot.domain.model.User
import com.ts.connectingdot.feature.editProfile.comp.ImageState
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo,
    private val storageRepo: StorageRepo
) : BaseViewModel() {

    val saveProfileTask = taskStateOf<Unit>()
    var currentUser: User? = null
    var email = mutableStateOf("")

    fun saveUser(user: User, image: MutableState<ImageState>, onSuccess: () -> Unit) {
        execute(showLoadingDialog = false) {

            saveProfileTask.load {

                val token = currentUser?.fcmToken ?: Firebase.messaging.token.await()

                var updatedUser = user.copy(
                    profileImageUrl = uploadProfileImage(user.email, image.value),
                    fcmToken = token,
                    id = currentUser?.id
                )

                updatedUser = userRepo.upsertUser(user = updatedUser)
                localRepo.upsertCurrentUser(updatedUser)
                executeOnMain {
                    onSuccess()
                }
            }

        }
    }

    fun getCurrentUser(
        onSuccess: (User) -> Unit,
        onNotFound:() -> Unit
    ) {
        execute(false) {
            localRepo.getLoggedInUserNullable()?.let { user ->
                currentUser = user
                onSuccess(user)
            } ?: kotlin.run (onNotFound)
        }
    }

    private suspend fun uploadProfileImage(email: String, image: ImageState): String? {

        return when(image){
            ImageState.Empty -> null
            is ImageState.Exists -> image.url
            is ImageState.New -> {
                storageRepo.uploadFile(
                    "profileImages/$email.jpg",
                    image.pickedMedia.uri.toUri()
                )
            }
        }
    }

}