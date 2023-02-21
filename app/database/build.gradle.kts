plugins {
    id(Plugins.Android.library)
    id(Plugins.Kotlin.kotlinLibrary)
    id(Plugins.sqlDelight) version(Plugins.Versions.sqlDelight)
}

android {
    namespace = Modules.Namespaces.database
    compileSdk = Android.compileSdk
}

sqldelight {
    database("AppDatabase") {
        packageName = "hardcoder.dev.database"
    }
}

dependencies {
    api(project(Modules.Paths.entities))
    addData()
    implementation(Dependencies.sqlDelightCoroutinesExt)
}