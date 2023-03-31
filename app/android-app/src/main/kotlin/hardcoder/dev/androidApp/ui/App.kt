package hardcoder.dev.androidApp.ui

import android.app.Application
import hardcoder.dev.androidApp.di.LogicModule
import hardcoder.dev.androidApp.di.PresentationModule
import hardcoder.dev.androidApp.di.UIModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {

    val presentationModule by lazy {
        PresentationModule(
            context = this,
            logicModule = logicModule
        )
    }
    val logicModule by lazy {
        LogicModule(context = this)
    }
    val uiModule by lazy {
        UIModule(context = this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        CoroutineScope(Dispatchers.IO).launch {
            logicModule.predefinedTracksManager.createPredefinedTracksIfNeeded()
        }
    }

    companion object {
        lateinit var instance: App
    }
}