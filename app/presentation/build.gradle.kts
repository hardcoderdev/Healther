plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = Modules.Namespaces.presentation
    compileSdk = Android.compileSdk

    defaultConfig {
        minSdk = Android.DefaultConfig.minSdk
    }
}

dependencies {
    api(projects.app.logic)
    api(projects.foundation.coroutines)
    api(projects.foundation.permissions)
    api(projects.foundation.inappreview)
    api(projects.foundation.controllers)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.compose.calendar.datetime)
}