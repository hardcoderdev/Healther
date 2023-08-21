package hardcoder.dev.logic.reward.experience

import app.cash.sqldelight.coroutines.asFlow
import hardcoder.dev.coroutines.BackgroundCoroutineDispatchers
import hardcoder.dev.coroutines.firstNotNull
import hardcoder.dev.database.AppDatabase
import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.logic.features.FeatureType
import hardcoder.dev.logic.features.FeatureTypeIdMapper
import hardcoder.dev.logic.hero.HeroProvider
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ExperienceCollector(
    heroProvider: HeroProvider,
    private val appDatabase: AppDatabase,
    private val dispatchers: BackgroundCoroutineDispatchers,
    private val featureTypeIdMapper: FeatureTypeIdMapper,
    private val heroExperiencePointsProvider: HeroExperiencePointsProvider,
    private val dateTimeProvider: DateTimeProvider,
) {
    private val currentHero = heroProvider.provideHero().filterNotNull()

    suspend fun collect(featureType: FeatureType) {
        val hero = currentHero.firstNotNull()

        val uncollectedExperienceSum = appDatabase.experienceQueries.provideAllExperiencesByDate(
            isCollected = false,
            featureTypeId = featureTypeIdMapper.mapToId(featureType),
            date = dateTimeProvider.currentDateRange().start,
            date_ = dateTimeProvider.currentDateRange().endInclusive,
        )
            .asFlow()
            .flowOn(dispatchers.io)
            .map {
                it.executeAsList().map {
                    it.experiencePointsAmount
                }.sum()
            }
            .flowOn(dispatchers.default)

        withContext(dispatchers.io) {
            val previousExperiencePoints = hero.experiencePoints
            val experiencePointsToIncrease = uncollectedExperienceSum.first()

            val increasedExperiencePoints = previousExperiencePoints + experiencePointsToIncrease
            val experienceNeedToNextLevel = heroExperiencePointsProvider.getExperiencePointsToNextLevel(currentLevel = hero.level)

            if (increasedExperiencePoints >= experienceNeedToNextLevel) {
                val experienceLeft = increasedExperiencePoints - experienceNeedToNextLevel

                appDatabase.heroQueries.updateLevel(level = hero.level + 1)
                appDatabase.heroQueries.updateExperience(experiencePoints = experienceLeft)
            } else {
                appDatabase.heroQueries.updateExperience(experiencePoints = increasedExperiencePoints)
            }
            appDatabase.experienceQueries.collect(
                featureTypeId = featureTypeIdMapper.mapToId(featureType = featureType),
            )
        }
    }
}