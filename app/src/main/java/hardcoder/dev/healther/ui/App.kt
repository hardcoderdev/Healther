package hardcoder.dev.healther.ui

import android.app.Application
import hardcoder.dev.healther.di.LogicModule
import hardcoder.dev.healther.di.PresentationModule

class App : Application() {

    val presentationModule by lazy {
        PresentationModule(
            LogicModule(this)
        )
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
    }
}