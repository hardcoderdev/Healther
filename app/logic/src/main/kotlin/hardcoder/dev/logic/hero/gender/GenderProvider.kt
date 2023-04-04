package hardcoder.dev.logic.hero.gender

import kotlinx.coroutines.flow.flowOf

class GenderProvider {

    fun provideAllGenders() = flowOf(Gender.values().asList())
}