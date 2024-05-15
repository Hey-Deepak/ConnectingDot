package com.ts.connectingdot.helper.di

import com.ts.connectingdot.data.LocalRepo
import com.ts.connectingdot.data.remote.StorageRepo
import com.ts.connectingdot.data.remote.UserRepo
import com.ts.connectingdot.feature.editProfile.EditProfileViewModel
import com.ts.connectingdot.feature.login.LoginViewModel
import com.ts.connectingdot.feature.splash.SplashViewModel
import com.ts.connectingdot.helper.DataStoreUtil
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { UserRepo() }
    single { LocalRepo(DataStoreUtil.create(context = get())) }
    single { StorageRepo() }
}

val viewModelModule = module{
    viewModel { SplashViewModel(get()) }
    viewModel { EditProfileViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
}