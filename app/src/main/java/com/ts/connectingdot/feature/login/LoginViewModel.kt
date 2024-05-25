package com.ts.connectingdot.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ts.connectingdot.data.LocalRepo
import com.ts.connectingdot.data.remote.UserRepo
import com.ts.connectingdot.helper.navigateTo
import com.ts.connectingdot.ui.Screens
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val localRepo: LocalRepo
): ViewModel() {

    fun onLoggedIn(email: String, navController: NavController){

        viewModelScope.launch {
            val user = userRepo.getUserWithEmail(email)
            if (user != null){
                    localRepo.onLoggedIn(user)
                    navController.navigate(Screens.Home.route)
            } else {
                navController.navigateTo(
                    Screens.EditProfile(email), Screens.Login
                )
            }

        }

    }


}