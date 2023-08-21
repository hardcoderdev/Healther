package hardcoder.dev.androidApp.ui

import android.app.Application
import hardcoder.dev.androidApp.di.dataModule
import hardcoder.dev.androidApp.di.logic.foundationLogicModule
import hardcoder.dev.androidApp.di.logicModule
import hardcoder.dev.androidApp.di.presentationModule
import hardcoder.dev.androidApp.di.uiModule
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import hardcoder.dev.logic.appPreferences.LastEntranceManager
import hardcoder.dev.logic.appPreferences.PredefinedTracksManager
import hardcoder.dev.logic.reward.penalty.PenaltyManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    private val dispatchers by inject<BackgroundCoroutineDispatchers>()
    private val appPreferenceProvider by inject<AppPreferenceProvider>()
    private val appPreference by lazy { appPreferenceProvider.provideAppPreference() }
    private val isFirstLaunch by lazy { appPreference.map { it == null } }
    private val lastEntranceManager by inject<LastEntranceManager>()
    private val penaltyManager by inject<PenaltyManager>()

    override fun onCreate() {
        super.onCreate()
        setUpKoin()
        CoroutineScope(dispatchers.io).launch {
            setUpPredefined()
            givePenaltiesIfNeed()
            lastEntranceManager.updateLastEntranceDate()
        }
    }

    private fun setUpKoin() {
        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@App)
            modules(
                foundationLogicModule,
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

    private suspend fun givePenaltiesIfNeed() {
        if (!isFirstLaunch.first() && lastEntranceManager.calculateEntranceDifferenceInDays() >= 1) {
            penaltyManager.collectPenalties()
        }
    }
}