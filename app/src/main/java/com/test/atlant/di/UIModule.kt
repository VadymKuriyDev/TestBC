package com.test.atlant.di

import com.test.atlant.NavigationLoginController
import com.test.atlant.NavigationMainController
import com.test.atlant.data.*
import com.test.atlant.ui.activity.LoginActivity
import com.test.atlant.ui.activity.MainActivity
import com.test.atlant.viewModel.LaunchViewModel
import com.test.atlant.viewModel.LoginViewModel
import com.test.atlant.viewModel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val navigationModule = module(override = true) {

    // single instance of NavigationMainController
    single<NavigationMainController> { get() }

    factory { (activity: MainActivity) -> NavigationMainController(activity)}
}

val navigationLoginModule = module(override = true) {

    single<NavigationLoginController> { get() }

    factory { (activity: LoginActivity) -> NavigationLoginController(activity)}
}

val viewModelModule = module {

    // LaunchViewModel
    viewModel { LaunchViewModel(get()) }

    // LoginViewModel
    viewModel { LoginViewModel(get()) }
    single<LoginRepository> { LoginRepositoryImpl(apiInterface = get(), tokenAuth = get()) }

    // MainViewModel
    viewModel { MainViewModel(get(), get()) }
    single<MainRepository> { MainRepositoryImpl(apiInterface = get(), tokenAuth = get()) }
    single<SocketRepository> { SocketRepositoryImpl(websocketAtlantService = get()) }
}