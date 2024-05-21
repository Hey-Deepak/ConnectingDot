package com.ts.connectingdot.helper.di

import com.ts.connectingdot.data.LocalRepo
import com.ts.connectingdot.data.remote.ChannelRepo
import com.ts.connectingdot.data.remote.StorageRepo
import com.ts.connectingdot.data.remote.UserRepo
import com.ts.connectingdot.feature.chat.ChatViewModel
import com.ts.connectingdot.feature.editProfile.EditProfileViewModel
import com.ts.connectingdot.feature.home.HomeViewModel
import com.ts.connectingdot.feature.login.LoginViewModel
import com.ts.connectingdot.feature.newChat.NewChatViewModel
import com.ts.connectingdot.feature.splash.SplashViewModel
import com.ts.connectingdot.helper.DataStoreUtil
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { UserRepo() }
    single { LocalRepo(DataStoreUtil.create(context = get())) }
    single { StorageRepo() }
    single { ChannelRepo() }
}

val viewModelModule = module{
    viewModel { SplashViewModel(get()) }
    viewModel { EditProfileViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { NewChatViewModel(get(), get(), get() ) }
    viewModel { HomeViewModel(get(), get(), get())}
    viewModel { ChatViewModel(get(), get()) }

}