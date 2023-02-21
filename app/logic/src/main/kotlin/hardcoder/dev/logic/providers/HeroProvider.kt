package hardcoder.dev.logic.providers

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.Hero
import kotlinx.coroutines.flow.map

class HeroProvider(private val appDatabase: AppDatabase) {

    fun requireHero() = appDatabase.heroQueries
        .selectCurrentHero()
        .asFlow()
        .map {
            it.executeAsOne().toEntity()
        }

    private fun Hero.toEntity() = hardcoder.dev.entities.Hero(
        id, weight, exerciseStressTime, gender
    )
}