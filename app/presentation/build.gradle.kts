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

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    api(project(Modules.Paths.logic))
    api(project(Modules.Paths.Foundation.coroutines))
    api(project(Modules.Paths.Foundation.permissions))
    api(project(Modules.Paths.Foundation.inAppReview))
    api(project(Modules.Paths.Foundation.controllers))

    implementation(Dependencies.lifecycleViewModelKtx)
    implementation(Dependencies.composeCalendarDateTime)
}