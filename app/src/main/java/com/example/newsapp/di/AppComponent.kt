package com.example.newsapp.di

import com.example.newsapp.di.viewmodel.ViewModelModule
import com.example.newsapp.presentation.auth.LoginFragment
import com.example.newsapp.presentation.main.detail.DetailActivity
import com.example.newsapp.presentation.main.home.HomeActivity
import com.example.newsapp.presentation.main.home.HomeFragment
import com.example.newsapp.presentation.welcome.WelcomeActivity
import dagger.Component

@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(welcomeActivity: WelcomeActivity)
    fun inject(loginFragment: LoginFragment)
    fun inject(homeActivity: HomeActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(detailActivity: DetailActivity)
}
