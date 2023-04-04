package hardcoder.dev.utilities

import android.os.Build

object VersionChecker {

    // 13
    fun isTiramisu() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    // 12
    fun isEasterEgg() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    // 11
    fun isRedVelvetCake() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

    // 10
    fun isQuinceTart() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    fun isOreo() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
}