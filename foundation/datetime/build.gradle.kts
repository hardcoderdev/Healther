plugins {
    id(Plugins.Android.library)
    id(Plugins.Kotlin.kotlinLibrary)
}

android {
    namespace = Modules.Namespaces.Foundation.datetime
    compileSdk = Android.compileSdk

    defaultConfig {
        multiDexEnabled = true
        minSdk = Android.DefaultConfig.minSdk
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    api(Dependencies.dateTime)
    api(Dependencies.composeCalendarDateTime)
}