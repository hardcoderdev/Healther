@file:Suppress("UnstableApiUsage")

plugins {
    id(Plugins.Android.library)
    id(Plugins.Kotlin.kotlinLibrary)
}

android {
    namespace = Modules.Namespaces.Foundation.uikit
    compileSdk = Android.compileSdk

    defaultConfig {
        minSdk = Android.DefaultConfig.minSdk
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Android.ComposeOptions.kotlinCompilerExtensionVersion
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    api(project(Modules.Paths.Foundation.controllers))
    addComposeMaterial()
    implementation(Dependencies.dateTime)
    implementation(Dependencies.composeCalendarDateTime)
}