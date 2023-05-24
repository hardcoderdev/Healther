object Modules {

    object Namespaces {
        const val database = "hardcoder.dev.database"
        const val presentation = "hardcoder.dev.presentation"
        const val logic = "hardcoder.dev.logic"

        object Foundation {
            const val permissions = "hardcoder.dev.permissions"
            const val coroutines = "hardcoder.dev.coroutines"
            const val uikit = "hardcoder.dev.uikit"
            const val utilities = "hardcoder.dev.utilities"
            const val sqldelight = "hardcoder.dev.sqldelight"
            const val datetime = "hardcoder.dev.datetime"
            const val math = "hardcoder.dev.math"
            const val inAppReview = "hardcoder.dev.inappreview"
        }
    }

    object Paths {
        val database = mapOf("path" to ":app:database")
        val presentation = mapOf("path" to ":app:presentation")
        val logic = mapOf("path" to ":app:logic")

        object Foundation {
            val permissions = mapOf("path" to ":foundation:permissions")
            val coroutines = mapOf("path" to ":foundation:coroutines")
            val uikit = mapOf("path" to ":foundation:uikit")
            val utilities = mapOf("path" to ":foundation:utilities")
            val sqldelight = mapOf("path" to ":foundation:sqldelight")
            val datetime = mapOf("path" to ":foundation:datetime")
            val math = mapOf("path" to ":foundation:math")
            val inAppReview = mapOf("path" to ":foundation:inappreview")
        }
    }
}