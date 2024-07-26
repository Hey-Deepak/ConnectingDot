package com.ts.connectingdot.feature.newGroupChat.comp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.streamliners.compose.comp.textInput.TextInputLayout
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.pickers.media.MediaPickerDialog
import com.streamliners.pickers.media.PickedMedia
import com.streamliners.pickers.media.rememberMediaPickerDialogState
import com.ts.connectingdot.feature.editProfile.comp.ProfileImage
import com.ts.connectingdot.helper.launchMediaPickerDialogForProfileImage
import com.ts.connectingdot.ui.comp.AddImageButton

@Composable
fun GroupInfoInput(
    nameInput: MutableState<TextInputState>,
    decriptionInput: MutableState<TextInputState>,
    image: MutableState<PickedMedia?>
) {

    val mediaPickerDialogState = rememberMediaPickerDialogState()



    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        image.value?.let {
            ProfileImage(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                data = it.uri,
                onClick = {
                    launchMediaPickerDialogForProfileImage(mediaPickerDialogState, scope, image)
                }
            )
        } ?: run {
            AddImageButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    launchMediaPickerDialogForProfileImage(mediaPickerDialogState, scope, image)
                }
            )
        }

        TextInputLayout(state = nameInput)



        TextInputLayout(state = decriptionInput)


    }

    MediaPickerDialog(
        state = mediaPickerDialogState,
        authority = "com.ts.connectingdot.fileprovider"
    )
    
}
