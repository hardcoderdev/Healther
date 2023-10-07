@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
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
        versionCatalogs {
            create("libs") {
                from(files("libs.versions.toml"))
            }
        }
    }
}

rootProject.name = "Healther"
include(
    ":app:android-app",
    ":app:resources",
    ":app:formatters",
    ":app:data:database",
    ":app:data:mock",
    ":app:logic",
    ":app:presentation",
)
include(
    ":foundation:coroutines",
    ":foundation:uikit",
    ":foundation:permissions",
    ":foundation:sqldelight",
    ":foundation:datetime",
    ":foundation:math",
    ":foundation:controllers",
    ":foundation:icons",
)
