package hardcoder.dev.database.entities.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val weight: Int,
    val exerciseStressTime: Int,
    val genderId: Int,
)
