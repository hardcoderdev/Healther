object Modules {

    object Namespaces {
        const val entities = "hardcoder.dev.entities"
        const val di = "hardcoder.dev.di"
        const val database = "hardcoder.dev.database"
        const val presentation = "hardcoder.dev.presentation"
        const val logic = "hardcoder.dev.logic"

        const val extensions = "hardcoder.dev.extensions"
        const val coroutines = "hardcoder.dev.coroutines"
        const val uikit = "hardcoder.dev.uikit"
    }

    object Paths {
        val entities = mapOf("path" to ":app:entities")
        val di = mapOf("path" to ":app:di")
        val database = mapOf("path" to ":app:database")
        val presentation = mapOf("path" to ":app:presentation")
        val logic = mapOf("path" to ":app:logic")

        val extensions = mapOf("path" to ":framework:extensions")
        val coroutines = mapOf("path" to ":framework:coroutines")
        val uikit = mapOf("path" to ":framework:uikit")
    }
}