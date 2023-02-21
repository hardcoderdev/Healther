plugins {
    id(Plugins.Android.library)
    id(Plugins.Kotlin.kotlinLibrary)
}

android {
    namespace = Modules.Namespaces.coroutines
    compileSdk = Android.compileSdk
}

dependencies {
    addCoroutines()
}