package hardcoder.dev.androidApp.ui

import android.app.Application
import hardcoder.dev.androidApp.di.LogicModule
import hardcoder.dev.androidApp.di.PresentationModule

class App : Application() {

    val presentationModule by lazy {
        PresentationModule(LogicModule(this))
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
    }
}