package hardcoder.dev.logic.hero

import hardcoder.dev.entities.hero.Gender
import kotlinx.coroutines.flow.flowOf

class GenderProvider {

    fun provideAllGenders() = flowOf(Gender.values().asList())
}