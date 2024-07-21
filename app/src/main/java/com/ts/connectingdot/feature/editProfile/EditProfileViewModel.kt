package com.ts.connectingdot.feature.editProfile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.net.toUri
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.ext.executeOnMain
import com.streamliners.base.ext.showToast
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf
import com.streamliners.compose.comp.textInput.config.InputConfig
import com.streamliners.compose.comp.textInput.config.text
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.compose.comp.textInput.state.update
import com.streamliners.pickers.media.PickedMedia
import com.ts.connectingdot.data.LocalRepo
import com.ts.connectingdot.data.remote.StorageRepo
import com.ts.connectingdot.data.remote.UserRepo
import com.ts.connectingdot.domain.model.Gender
import com.ts.connectingdot.domain.model.User
import com.ts.connectingdot.domain.model.ext.id
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo,
    private val storageRepo: StorageRepo
) : BaseViewModel() {

    val nameInput =
        mutableStateOf(
            TextInputState(
                label = "Name",
                inputConfig = InputConfig.text {
                    minLength = 5
                    maxLength = 30
                }
            )
        )

    val bioInput =
        mutableStateOf(
            TextInputState(
                label = "Bio",
                inputConfig = InputConfig.text {
                    minLength = 3
                    maxLength = 100
                }
            )
        )


    val gender =  mutableStateOf<Gender?>(null)

    val dob = mutableStateOf<String?>(null)

    val image = mutableStateOf<ImageState?>(null)
    val id = mutableStateOf<String?>(null)


    val saveProfileTask = taskStateOf<Unit>()


    fun loadProfile(){
        execute(showLoadingDialog = true) {
            if (localRepo.isLoggedIn()){
                val user = localRepo.getLoggedInUser()
                nameInput.update(user.name)
                bioInput.update(user.bio)
                gender.value = user.gender
                dob.value = user.dob
                id.value = user.id()
                if (user.profileImageUrl is String){
                    image.value = ImageState.Url(user.profileImageUrl)
                }
            }
        }

    }

    fun saveUser(user: User, onSuccess: () -> Unit) {

        execute(showLoadingDialog = false) {

            saveProfileTask.load {

                val token = Firebase.messaging.token.await()
                val image = image.value
                when(image){
                    is ImageState.New -> {
                        var updatedUser = user.copy(
                            profileImageUrl = uploadProfileImage(user.email, image.media),
                            fcmToken = token
                        )
                        updatedUser = userRepo.saveUser(user = updatedUser)
                        localRepo.upsertCurrentUser(updatedUser)
                        executeOnMain {
                            onSuccess()
                        }
                    }
                    is ImageState.Url -> {
                        var updatedUser = user.copy(
                            profileImageUrl = image.url,
                            fcmToken = token
                        )
                        updatedUser = userRepo.saveUser(updatedUser)
                        localRepo.upsertCurrentUser(updatedUser)
                        executeOnMain {
                            onSuccess()
                        }
                    }
                    null -> {
                        showToast("Image not found")
                        var updatedUser = user.copy(
                            profileImageUrl = null,
                            fcmToken = token
                        )
                        updatedUser = userRepo.saveUser(updatedUser)
                        localRepo.upsertCurrentUser(updatedUser)
                        executeOnMain {
                            onSuccess()
                        }
                    }
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