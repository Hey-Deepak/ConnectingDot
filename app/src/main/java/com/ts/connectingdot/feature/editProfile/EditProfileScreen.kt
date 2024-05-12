package com.ts.connectingdot.feature.editProfile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.streamliners.compose.comp.select.RadioGroup
import com.streamliners.compose.comp.textInput.TextInputLayout
import com.streamliners.compose.comp.textInput.config.InputConfig
import com.streamliners.compose.comp.textInput.config.text
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.compose.comp.textInput.state.allHaveValidInputs
import com.streamliners.compose.comp.textInput.state.value
import com.ts.connectingdot.domain.model.Gender
import com.ts.connectingdot.domain.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    email: String
) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "EditProfile")})
        }
    ) { paddingValues ->

        val context = LocalContext.current

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


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {


            
            TextInputLayout(state = nameInput)

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
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
                }
            }

            Button(onClick = {

                if(
                    TextInputState.allHaveValidInputs(
                        nameInput, bioInput
                    )
                ){

                    gender.value?.let { gender ->
                        Toast.makeText(context, "Sahi Jawab", Toast.LENGTH_LONG).show()

                        val user = User(
                            name = nameInput.value(),
                            email = email,
                            bio = bioInput.value(),
                            gender = gender
                        )
                    }

                } else {
                    Toast.makeText(context, "Galat Jawab", Toast.LENGTH_LONG).show()
                }

            }) {
                Text(text = "SAVE")
            }

        }

    }

}