package hardcoder.dev.androidApp.ui

import android.app.Application
import hardcoder.dev.androidApp.di.LogicModule
import hardcoder.dev.androidApp.di.PresentationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {

    private val predefinedTracksManager by lazy { logicModule.predefinedTracksManager }
    private val logicModule by lazy { LogicModule(this) }
    val presentationModule by lazy { PresentationModule(logicModule) }

    override fun onCreate() {
        super.onCreate()
        instance = this
        CoroutineScope(Dispatchers.IO).launch {
            predefinedTracksManager.createPredefinedTracksIfNeeded()
        }
    }

    companion object {
        lateinit var instance: App
    }
}