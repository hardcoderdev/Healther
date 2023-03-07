package hardcoder.dev.android_app.ui

import android.app.Application
import hardcoder.dev.android_app.di.LogicModule
import hardcoder.dev.android_app.di.PresentationModule

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