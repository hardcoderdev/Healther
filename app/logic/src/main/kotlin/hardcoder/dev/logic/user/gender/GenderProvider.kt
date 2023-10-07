package hardcoder.dev.logic.user.gender

import kotlinx.coroutines.flow.flowOf

class GenderProvider {

    fun provideAllGenders() = flowOf(Gender.entries)
}