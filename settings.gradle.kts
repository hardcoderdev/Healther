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
include(
    ":app:android-app",
    ":app:database",
    ":app:logic",
    ":app:presentation"
)
include(
    ":framework:extensions",
    ":framework:coroutines",
    ":framework:uikit",
    ":framework:utilities",
    ":framework:permissions"
)