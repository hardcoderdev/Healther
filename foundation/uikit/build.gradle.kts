@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlin.android)
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
}

dependencies {
    api(projects.foundation.controllers)
    implementation(platform(libs.compose.bom))
    implementation(libs.kotlin.datetime)
    implementation(libs.compose.calendar.datetime)
    implementation(libs.compose.activity)
    implementation(libs.compose.material)
    implementation(libs.compose.ui)
    implementation(libs.compose.extended.icons)
    implementation(libs.compose.charts)
    implementation(libs.compose.tooling)
    implementation(libs.compose.number.picker)
    implementation(libs.compose.calendar)
    implementation(libs.compose.calendar.datetime)
    implementation(libs.compose.accompanist.viewpager)
    implementation(libs.compose.accompanist.viewpager.indicators)
    implementation(libs.compose.lottie)
}