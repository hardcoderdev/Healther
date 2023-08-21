package hardcoder.dev.database.appAdapters

import app.cash.sqldelight.adapter.primitive.FloatColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import hardcoder.dev.database.Hero

object HeroAdapters {

    fun createHeroAdapter() = Hero.Adapter(
        idAdapter = IntColumnAdapter,
        weightAdapter = IntColumnAdapter,
        exerciseStressTimeAdapter = IntColumnAdapter,
        genderIdAdapter = IntColumnAdapter,
        currentHealthPointsAdapter = IntColumnAdapter,
        maxHealthPointsAdapter = IntColumnAdapter,
        coinsAdapter = FloatColumnAdapter,
        crystalsAdapter = IntColumnAdapter,
        levelAdapter = IntColumnAdapter,
        experiencePointsAdapter = FloatColumnAdapter,
    )
}