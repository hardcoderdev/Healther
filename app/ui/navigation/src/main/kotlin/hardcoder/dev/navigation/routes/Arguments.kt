package hardcoder.dev.navigation.routes

import androidx.navigation.NavType
import androidx.navigation.navArgument

fun intNavArg(name: String) = navArgument(name) {
    type = NavType.IntType
}

fun stringNavArg(name: String) = navArgument(name) {
    type = NavType.StringType
}