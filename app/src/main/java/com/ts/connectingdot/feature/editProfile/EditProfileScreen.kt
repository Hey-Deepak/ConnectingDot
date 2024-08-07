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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.streamliners.base.taskState.comp.TaskLoadingButton
import com.streamliners.compose.comp.select.RadioGroup
import com.streamliners.compose.comp.textInput.TextInputLayout
import com.streamliners.compose.comp.textInput.config.InputConfig
import com.streamliners.compose.comp.textInput.config.text
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.compose.comp.textInput.state.allHaveValidInputs
import com.streamliners.compose.comp.textInput.state.update
import com.streamliners.compose.comp.textInput.state.value
import com.streamliners.pickers.date.DatePickerDialog
import com.streamliners.pickers.date.ShowDatePicker
import com.streamliners.pickers.media.MediaPickerDialog
import com.streamliners.pickers.media.rememberMediaPickerDialogState
import com.streamliners.utils.DateTimeUtils.Format.Companion.DATE_MONTH_YEAR_2
import com.ts.connectingdot.domain.model.Gender
import com.ts.connectingdot.domain.model.User
import com.ts.connectingdot.feature.editProfile.comp.ImageState
import com.ts.connectingdot.ui.comp.AddImageButton
import com.ts.connectingdot.feature.editProfile.comp.ProfileImage
import com.ts.connectingdot.helper.launchMediaPickerDialogForProfileImage
import com.ts.connectingdot.helper.navigateTo
import com.ts.connectingdot.ui.Screens
import com.ts.connectingdot.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel,
    email: String?,
    showDatePicker: ShowDatePicker
) {

    val mediaPickerDialogState = rememberMediaPickerDialogState()

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = "EditProfile") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White
                ))
        }
    ) { paddingValues ->

        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        val imageState = remember {
            mutableStateOf<ImageState>(ImageState.Empty)
        }

        val nameInput = remember {
            mutableStateOf(
                TextInputState(
                    label = "Name",
                    inputConfig = InputConfig.text {
                        minLength = 5
                        maxLength = 30
                    }
                )
            )
        }

        val bioInput = remember {
            mutableStateOf(
                TextInputState(
                    label = "Bio",
                    inputConfig = InputConfig.text {
                        minLength = 3
                        maxLength = 100
                    }
                )
            )
        }

        val gender = remember { mutableStateOf<Gender?>(null) }
        val genderError = remember { mutableStateOf(false) }
        val dob = remember { mutableStateOf<String?>(null) }

        LaunchedEffect(key1 = gender.value) {
            if (gender.value != null) genderError.value = false
        }

        // Prefill value based on current user object
        LaunchedEffect(key1 = Unit) {
            viewModel.getCurrentUser(
                onSuccess = { user ->
                    user.run {
                        nameInput.update(name)
                        bioInput.update(bio)
                    }
                    gender.value = user.gender
                    dob.value = user.dob

                    user.profileImageUrl?.let { url ->
                        imageState.value = ImageState.Exists(url)
                    }
                    viewModel.email.value = user.email
                },
                onNotFound = {
                    viewModel.email.value = email ?: error("Email arg  not found")
                }
            )
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


            imageState.value.data()?.let {
                ProfileImage(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    data = it,
                    onClick = {
                        launchMediaPickerDialogForProfileImage(
                            mediaPickerDialogState,
                            scope,
                            imageState
                        )
                    }
                )
            } ?: run {
                AddImageButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        launchMediaPickerDialogForProfileImage(
                            mediaPickerDialogState,
                            scope,
                            imageState
                        )
                    }
                )
            }



            TextInputLayout(state = nameInput)

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.email.value,
                onValueChange = {},
                enabled = false,
                label = { Text(text = "Email") }
            )

            TextInputLayout(state = bioInput)

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
                        state = gender,
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
                                prefill = dob.value,
                                onPicked = { date ->
                                    dob.value = date
                                }
                            )
                        )
                    },
                value = dob.value ?: "",
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
                            nameInput, bioInput
                        )
                    ) {

                        gender.value?.let { gender ->
                            Toast.makeText(context, "Sahi Jawab", Toast.LENGTH_LONG).show()

                            val user = User(
                                name = nameInput.value(),
                                email = viewModel.email.value,
                                profileImageUrl = null,
                                bio = bioInput.value(),
                                gender = gender,
                                dob = dob.value,
                                fcmToken = null
                            )

                            viewModel.saveUser(user = user, imageState) {
                                Toast.makeText(context, "Saved", Toast.LENGTH_LONG)
                                    .show()
                                navController.navigateTo(
                                    Screens.Home.route,
                                    Screens.EditProfile.format()
                                )
                            }


                        }
                    }

                    if (gender.value == null) {
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


