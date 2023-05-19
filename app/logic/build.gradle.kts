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

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    api(project(Modules.Paths.database))
    api(project(Modules.Paths.Foundation.datetime))
    api(project(Modules.Paths.Foundation.math))
    api(project(Modules.Paths.Foundation.sqldelight))
    implementation(Dependencies.dataStore)
    addData()
    addCoroutines()
}