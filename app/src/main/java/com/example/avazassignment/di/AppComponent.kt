package com.example.avazassignment.di

import com.example.avazassignment.app
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        AppModule::class, OnBoardingActivityModule::class
    ]
)
interface AppComponent {
    fun inject(app: app)
}