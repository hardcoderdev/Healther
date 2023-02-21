package hardcoder.dev.android_ui

import android.app.Application
import hardcoder.dev.di.LogicModule
import hardcoder.dev.di.PresentationModule

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