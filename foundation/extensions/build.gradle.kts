plugins {
    id(Plugins.Android.library)
    id(Plugins.Kotlin.kotlinLibrary)
}

android {
    namespace = Modules.Namespaces.extensions
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
    addCoroutines()
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.dateTime)
}