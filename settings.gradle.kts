@file:Suppress("UnstableApiUsage")

include(":app:ui:pedometer-manager")


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
    ":android-app",
    ":app:ui:navigation",
    ":app:ui:screens",
    ":app:ui:resources",
    ":app:ui:formatters",
    ":app:ui:statistics-resolvers",
    //"app:ui:pedometer-manager",
    ":app:di",
    ":app:presentation",
    ":app:data:database",
    ":app:data:mock",
    ":app:domain:logics",
    ":app:domain:mappers",
    ":app:domain:resolvers",
    ":app:domain:validators",
    ":app:domain:entities",
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
    ":foundation:identification",
)
