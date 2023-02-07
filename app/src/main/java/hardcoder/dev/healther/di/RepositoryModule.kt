package hardcoder.dev.healther.di

import android.content.Context
import hardcoder.dev.healther.repository.UserRepository

class RepositoryModule(private val context: Context) {

    val userRepository by lazy {
        UserRepository(context)
    }
}