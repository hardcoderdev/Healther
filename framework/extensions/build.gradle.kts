plugins {
    id(Plugins.Android.library)
    id(Plugins.Kotlin.kotlinLibrary)
}

android {
    namespace = Modules.Namespaces.extensions
    compileSdk = Android.compileSdk
}

dependencies {
    addCoroutines()
    implementation(Dependencies.dateTime)
}