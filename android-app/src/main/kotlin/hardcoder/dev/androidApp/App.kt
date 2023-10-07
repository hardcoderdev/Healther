package hardcoder.dev.androidApp

import android.app.Application
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.di.dataModule
import hardcoder.dev.di.foundationModule
import hardcoder.dev.di.logicModule
import hardcoder.dev.di.presentationModule
import hardcoder.dev.di.uiModule
import hardcoder.dev.logic.appPreferences.PredefinedTracksManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    private val dispatchers by inject<BackgroundCoroutineDispatchers>()

    override fun onCreate() {
        super.onCreate()
        setUpKoin()
        CoroutineScope(dispatchers.io).launch {
            setUpPredefined()
        }
    }

    private fun setUpKoin() {
        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@App)
            modules(
                foundationModule,
                dataModule,
                logicModule,
                presentationModule,
                uiModule,
            )
        }
    }

    private suspend fun setUpPredefined() {
        CoroutineScope(dispatchers.io).launch {
            get<PredefinedTracksManager>().createPredefinedTracksIfNeeded()
        }
    }
}