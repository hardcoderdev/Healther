package hardcoder.dev.logic.hero

import com.squareup.sqldelight.runtime.coroutines.asFlow
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.Hero
import hardcoder.dev.logic.hero.gender.GenderIdMapper
import kotlinx.coroutines.flow.map
import hardcoder.dev.logic.hero.Hero as HeroEntity

class HeroProvider(
    private val appDatabase: AppDatabase,
    private val genderIdMapper: GenderIdMapper
) {

    fun requireHero() = appDatabase.heroQueries
        .provideCurrentHero()
        .asFlow()
        .map {
            it.executeAsOne().toEntity()
        }

    private fun Hero.toEntity() = HeroEntity(
        weight, exerciseStressTime, genderIdMapper.mapToGender(genderId)
    )
}