object Modules {

    object Namespaces {

        const val entities = "hardcoder.dev.entities"
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
        val entities = mapOf("path" to ":app:entities")
        val database = mapOf("path" to ":app:database")
        val presentation = mapOf("path" to ":app:presentation")
        val logic = mapOf("path" to ":app:logic")

        val extensions = mapOf("path" to ":framework:extensions")
        val permissions = mapOf("path" to ":framework:permissions")
        val coroutines = mapOf("path" to ":framework:coroutines")
        val uikit = mapOf("path" to ":framework:uikit")
        val utilities = mapOf("path" to ":framework:utilities")
    }
}