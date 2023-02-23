plugins {
    id(Plugins.Android.library)
    id(Plugins.Kotlin.kotlinLibrary)
}

android {
    namespace = Modules.Namespaces.presentation
    compileSdk = Android.compileSdk

    defaultConfig {
        minSdk = Android.DefaultConfig.minSdk
    }
}

dependencies {
    api(project(Modules.Paths.logic))
    api(project(Modules.Paths.coroutines))
    api(project(Modules.Paths.extensions))

    implementation(Dependencies.lifecycleViewModelKtx)
    implementation(Dependencies.composeCalendarDateTime)
}