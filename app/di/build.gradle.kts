plugins {
    id(Plugins.Android.library)
    id(Plugins.Kotlin.kotlinLibrary)
}

android {
    namespace = Modules.Namespaces.di
    compileSdk = Android.compileSdk
}

dependencies {
    api(project(Modules.Paths.logic))
    api(project(Modules.Paths.presentation))
    addCoroutines()
}