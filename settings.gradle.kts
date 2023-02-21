pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Healther"
include(":app")
include(":app:entities")
include(":app:database")
include(":app:di")
include(":app:logic")
include(":app:presentation")
include(":framework:extensions")
include(":framework:coroutines")
include(":framework:uikit")
include(":app:android-ui")
