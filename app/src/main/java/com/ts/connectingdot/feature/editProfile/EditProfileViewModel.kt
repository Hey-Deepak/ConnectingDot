package com.ts.connectingdot.feature.editProfile

import androidx.compose.runtime.MutableState
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch
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
                var updatedUser = user.copy(
                    profileImageUrl = uploadProfileImage(user.email, image.value)
                )
                updatedUser = userRepo.saveUser(user = updatedUser)
                localRepo.onLoggedIn(updatedUser)
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