plugins {
    id(Plugins.Android.library)
    id(Plugins.Kotlin.kotlinLibrary)
}

android {
    namespace = Modules.Namespaces.entities
    compileSdk = Android.compileSdk
}