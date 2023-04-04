object Modules {

    object Namespaces {
        const val database = "hardcoder.dev.database"
        const val presentation = "hardcoder.dev.presentation"
        const val logic = "hardcoder.dev.logic"

        const val extensions = "hardcoder.dev.extensions"
        const val permissions = "hardcoder.dev.permissions"
        const val coroutines = "hardcoder.dev.coroutines"
        const val uikit = "hardcoder.dev.uikit"
        const val utilities = "hardcoder.dev.utilities"
    }

    object Paths {
        val database = mapOf("path" to ":app:database")
        val presentation = mapOf("path" to ":app:presentation")
        val logic = mapOf("path" to ":app:logic")

        val extensions = mapOf("path" to ":foundation:extensions")
        val permissions = mapOf("path" to ":foundation:permissions")
        val coroutines = mapOf("path" to ":foundation:coroutines")
        val uikit = mapOf("path" to ":foundation:uikit")
        val utilities = mapOf("path" to ":foundation:utilities")
    }
}