package com.ts.connectingdot.feature.editProfile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.streamliners.base.ext.showToast
import com.streamliners.base.taskState.comp.TaskLoadingButton
import com.streamliners.compose.comp.select.RadioGroup
import com.streamliners.compose.comp.textInput.TextInputLayout
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.compose.comp.textInput.state.allHaveValidInputs
import com.streamliners.compose.comp.textInput.state.value
import com.streamliners.pickers.date.DatePickerDialog
import com.streamliners.pickers.date.ShowDatePicker
import com.streamliners.pickers.media.FromGalleryType
import com.streamliners.pickers.media.MediaPickerDialog
import com.streamliners.pickers.media.MediaPickerDialogState
import com.streamliners.pickers.media.MediaType
import com.streamliners.pickers.media.rememberMediaPickerDialogState
import com.streamliners.utils.DateTimeUtils.Format.DATE_MONTH_YEAR_2
import com.ts.connectingdot.domain.model.Gender
import com.ts.connectingdot.domain.model.User
import com.ts.connectingdot.feature.editProfile.comp.AddImageButton
import com.ts.connectingdot.feature.editProfile.comp.ProfileImage
import com.ts.connectingdot.helper.navigateTo
import com.ts.connectingdot.ui.Screens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel,
    email: String,
    showDatePicker: ShowDatePicker
) {

    val mediaPickerDialogState = rememberMediaPickerDialogState()

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(title = { Text(text = "EditProfile") })
        }
    ) { paddingValues ->

        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        val genderError = remember { mutableStateOf(false) }
        val image = viewModel.image.value

        LaunchedEffect(key1 = viewModel.gender.value) {
            if (viewModel.gender.value != null) genderError.value = false
        }
        LaunchedEffect(key1 = Unit) {
            viewModel.loadProfile()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val initImagePicker = {
                mediaPickerDialogState.value = MediaPickerDialogState.Visible(
                    type = MediaType.Image,
                    allowMultiple = false,
                    fromGalleryType = FromGalleryType.VisualMediaPicker
                ) { getList ->
                    scope.launch {
                        val list = getList()
                        list.firstOrNull()?.let {
                            viewModel.image.value = ImageState.New(it)
                        }
                    }
                }
            }

            when(image){
                is ImageState.New -> {


                    viewModel.image.value?.let {
                        image.media?.let { media ->
                            ProfileImage(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                pickedMedia = media.uri,
                                onClick = initImagePicker
                            )
                        }
                    } ?: run {
                        AddImageButton(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = initImagePicker
                        )
                    }
                }
                is ImageState.Url -> {
                    image.url?.let { url ->
                        ProfileImage(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            pickedMedia = url,
                            onClick = {}
                        )
                    }
                }
                null -> {
                    viewModel.showToast("No Image Found")
                    AddImageButton(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = initImagePicker
                    )
                }
            }





            TextInputLayout(state = viewModel.nameInput)

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = {},
                enabled = false,
                label = { Text(text = "Email") }
            )

            TextInputLayout(state = viewModel.bioInput)

            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        )
                ) {
                    RadioGroup(
                        title = "Gender",
                        state = viewModel.gender,
                        options = Gender.entries.toList(),
                        labelExtractor = { it.name }
                    )

                    if (genderError.value) {
                        Text(text = "Required")
                    }
                }


            }

            // TODO: Min, Max Date
            // TODO: Make DOB Compulsory

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDatePicker(
                            DatePickerDialog.Params(
                                format = DATE_MONTH_YEAR_2,
                                prefill = viewModel.dob.value,
                                onPicked = { date ->
                                    viewModel.dob.value = date
                                }
                            )
                        )
                    },
                value = viewModel.dob.value ?: "",
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = { Text(text = "Date of Birth") }
            )

            TaskLoadingButton(
                state = viewModel.saveProfileTask,
                label = "SAVE",
                onClick = {

                if (
                    TextInputState.allHaveValidInputs(
                        viewModel.nameInput, viewModel.bioInput
                    )
                ) {

                    viewModel.gender.value?.let { gender ->
                        Toast.makeText(context, "Sahi Jawab", Toast.LENGTH_LONG).show()

                        val user: User
                        if (viewModel.image.value != null){
                            val imageOld = viewModel.image.value as ImageState.Url
                            user = User(
                                id = viewModel.id.value,
                                name = viewModel.nameInput.value(),
                                email = email,
                                profileImageUrl = imageOld.url,
                                bio = viewModel.bioInput.value(),
                                gender = gender,
                                dob = viewModel.dob.value,
                                fcmToken = null
                            )
                        } else {
                            user = User(
                                id = viewModel.id.value,
                                name = viewModel.nameInput.value(),
                                email = email,
                                profileImageUrl = null,
                                dob = viewModel.dob.value,
                                bio = viewModel.bioInput.value(),
                                gender = gender,
                                fcmToken = null
                            )
                        }

                        viewModel.saveUser(user = user) {
                            Toast.makeText(context, "User Profile Created", Toast.LENGTH_LONG)
                                .show()
                            navController.navigateTo(Screens.Home.route, Screens.EditProfile.format())
                        }


                    }
                }

                if (viewModel.gender.value == null) {
                    genderError.value = true
                }

            })

        }

    }

    MediaPickerDialog(
        state = mediaPickerDialogState,
        authority = "com.ts.connectingdot.fileprovider"
    )

}
