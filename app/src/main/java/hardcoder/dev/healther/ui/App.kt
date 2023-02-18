package hardcoder.dev.healther.ui

import android.app.Application
import hardcoder.dev.healther.di.DatabaseModule
import hardcoder.dev.healther.di.LogicModule
import hardcoder.dev.healther.di.PresentationModule
import hardcoder.dev.healther.di.RepositoryModule

class App : Application() {

    val presentationModule by lazy {
        PresentationModule(
            LogicModule(),
            RepositoryModule(
                this,
                DatabaseModule(this)
            )
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