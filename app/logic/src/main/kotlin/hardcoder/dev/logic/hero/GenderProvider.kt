package hardcoder.dev.logic.hero

import hardcoder.dev.logic.entities.hero.Gender
import kotlinx.coroutines.flow.flowOf

class GenderProvider {

    fun provideAllGenders() = flowOf(Gender.values().asList())
}