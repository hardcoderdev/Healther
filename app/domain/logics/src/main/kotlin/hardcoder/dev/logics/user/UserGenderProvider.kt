package hardcoder.dev.logic.user

import hardcoder.dev.entities.user.Gender
import kotlinx.coroutines.flow.flowOf

class UserGenderProvider {

    fun provideAllGenders() = flowOf(Gender.entries)
}