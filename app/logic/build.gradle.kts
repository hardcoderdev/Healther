plugins {
    id(Plugins.Android.library)
    id(Plugins.Kotlin.kotlinLibrary)
}

android {
    namespace = Modules.Namespaces.logic
    compileSdk = Android.compileSdk

    defaultConfig {
        minSdk = Android.DefaultConfig.minSdk
    }
}

dependencies {
    api(project(Modules.Paths.database))
    api(project(Modules.Paths.extensions))
    implementation(Dependencies.dateTime)
    addData()
    addCoroutines()
}