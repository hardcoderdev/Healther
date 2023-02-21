plugins {
    id(Plugins.Android.library)
    id(Plugins.Kotlin.kotlinLibrary)
}

android {
    namespace = Modules.Namespaces.logic
    compileSdk = Android.compileSdk
}

dependencies {
    api(project(Modules.Paths.database))
    addData()
    addCoroutines()
}