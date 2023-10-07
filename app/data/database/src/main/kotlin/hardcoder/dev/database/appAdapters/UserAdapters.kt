package hardcoder.dev.database.appAdapters

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import hardcoder.dev.database.User

object UserAdapters {

    fun createUserAdapter() = User.Adapter(
        idAdapter = IntColumnAdapter,
        weightAdapter = IntColumnAdapter,
        exerciseStressTimeAdapter = IntColumnAdapter,
        genderIdAdapter = IntColumnAdapter,
    )
}