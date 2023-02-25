package hardcoder.dev.utilities

import android.os.Build

object VersionChecker {

    // 13
    fun isTiramisu() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    // 12
    fun isEasterEgg() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    // 11
    fun isRedVelvetCake() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

    fun isOreo() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    // 7 or lower
    fun isNougatOrLower() = Build.VERSION.SDK_INT <= Build.VERSION_CODES.N
}