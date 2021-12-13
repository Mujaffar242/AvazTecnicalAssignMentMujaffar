package com.example.avazassignment

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.avazassignment.di.AppModule
import com.example.avazassignment.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class app:Application() ,HasAndroidInjector{

    @Inject lateinit var mInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        return mInjector
    }

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder().appModule(AppModule()).build().inject(this)
    }


}