package hardcoder.dev.logic.hero

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.database.Hero
import hardcoder.dev.logic.hero.gender.GenderIdMapper
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import hardcoder.dev.logic.hero.Hero as HeroEntity

class HeroProvider(
    private val appDatabase: AppDatabase,
    private val genderIdMapper: GenderIdMapper,
    private val dispatchers: BackgroundCoroutineDispatchers,
) {

    fun provideHero() = appDatabase.heroQueries
        .provideCurrentHero()
        .asFlow()
        .map { it.executeAsOneOrNull()?.toEntity() }
        .flowOn(dispatchers.io)

    private fun Hero.toEntity() = HeroEntity(
        id = id,
        weight = weight,
        exerciseStressTime = exerciseStressTime,
        gender = genderIdMapper.mapToGender(genderId),
        name = name,
        currentHealthPoints = currentHealthPoints,
        maxHealthPoints = maxHealthPoints,
        experiencePoints = experiencePoints,
        level = level,
        coins = coins,
        crystals = crystals,
    )
}