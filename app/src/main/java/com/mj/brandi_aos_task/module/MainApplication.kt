package com.mj.brandi_aos_task.module

import android.app.Application
import org.koin.core.context.startKoin

class MainApplication: Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(retrofitApiConnectionModule())
        }
    }
}