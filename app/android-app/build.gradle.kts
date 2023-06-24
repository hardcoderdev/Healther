@file:Suppress("UnstableApiUsage")

import dev.icerock.gradle.MRVisibility

plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.moko.resources)
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

multiplatformResources {
    multiplatformResourcesPackage = "hardcoder.dev.healther" // required
    multiplatformResourcesClassName = "SharedRes" // optional, default MR
    multiplatformResourcesVisibility = MRVisibility.Internal // optional, default Public
    iosBaseLocalizationRegion = "ru" // optional, default "en"
    multiplatformResourcesSourceSet = "commonClientMain"  // optional, default "commonMain"
}

dependencies {
    implementation(projects.app.presentation)
    implementation(projects.foundation.uikit)
    implementation(platform(libs.compose.bom))
    implementation(libs.moko.resources.compose)
    implementation(libs.koin.di)
    implementation(libs.koin.di.compose)
    implementation(libs.compose.activity)
    implementation(libs.bundles.voyager.navigation)
    implementation(libs.compose.graphics)
    implementation(libs.compose.tooling)
    implementation(libs.compose.accompanist.flowrow)
    implementation(libs.compose.calendar)
    implementation(libs.compose.coil)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.core.ktx)
    implementation(libs.activity.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    debugImplementation(libs.compose.debug.tooling)
}