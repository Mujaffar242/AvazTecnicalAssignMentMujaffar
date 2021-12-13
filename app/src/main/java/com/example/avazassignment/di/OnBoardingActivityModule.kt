package com.example.avazassignment.di

import com.example.avazassignment.view.activities.HomeActivity
import com.example.avazassignment.view.activities.OnBoardingActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class OnBoardingActivityModule {

    @ContributesAndroidInjector
    abstract fun contriButeOnBoardingActivityInjector():OnBoardingActivity

    @ContributesAndroidInjector
    abstract fun contriHomeActivityInjector():HomeActivity
}