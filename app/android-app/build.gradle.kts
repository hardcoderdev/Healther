@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.navigation.safe.args)
    id("android-compose-convention")
    id("android-app-convention")
}

android {
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles("proguard-android-optimize.txt", "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(projects.app.presentation)
    implementation(projects.foundation.uikit)
    implementation(platform(libs.compose.bom))
    implementation(libs.koin.di)
    implementation(libs.koin.di.compose)
    implementation(libs.compose.graphics)
    implementation(libs.compose.navigation)
    implementation(libs.compose.tooling)
    implementation(libs.compose.accompanist.flowrow)
    implementation(libs.compose.calendar)
    implementation(libs.compose.calendar.datetime)
    implementation(libs.compose.coil)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.core.ktx)
    implementation(libs.activity.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    debugImplementation(libs.compose.debug.tooling)
}