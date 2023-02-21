plugins {
    id(Plugins.Android.library)
    id(Plugins.Kotlin.kotlinLibrary)
}

android {
    namespace = Modules.Namespaces.uikit
    compileSdk = Android.compileSdk

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Android.ComposeOptions.kotlinCompilerExtensionVersion
    }
}

dependencies {
    addCompose()
}