package com.ts.connectingdot.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ts.connectingdot.data.LocalRepo
import com.ts.connectingdot.ui.Screens
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val localRepo: LocalRepo
) : ViewModel() {

    fun checkLoginStatus(
        navController: NavController
    ){

        viewModelScope.launch {
            if (localRepo.isLoggedIn()){
                navController.navigate(Screens.Home.route)
            } else {
                navController.navigate(Screens.Login.route)
            }
        }

    }

}