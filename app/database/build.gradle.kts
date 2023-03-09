plugins {
    id(Plugins.Android.library)
    id(Plugins.Kotlin.kotlinLibrary)
    id(Plugins.sqlDelight) version (Plugins.Versions.sqlDelight)
}

android {
    namespace = Modules.Namespaces.database
    compileSdk = Android.compileSdk

    defaultConfig {
        minSdk = Android.DefaultConfig.minSdk
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "hardcoder.dev.database"
        dialect = "sqlite:3.24"
    }
}

dependencies {
    api(project(Modules.Paths.entities))
    addData()
    implementation(Dependencies.dateTime)
    implementation(Dependencies.sqlDelightCoroutinesExt)
}