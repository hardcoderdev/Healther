@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.navigation.safe.args)
}

android {
    compileSdk = Android.compileSdk
    namespace = Android.DefaultConfig.applicationId

    defaultConfig {
        applicationId = Android.DefaultConfig.applicationId
        minSdk = Android.DefaultConfig.minSdk
        targetSdk = Android.DefaultConfig.targetSdk
        versionCode = Android.DefaultConfig.versionCode
        versionName = Android.DefaultConfig.versionName
        multiDexEnabled = true
        testInstrumentationRunner = Android.DefaultConfig.instrumentationRunner

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        all {
            proguardFiles(
                getDefaultProguardFile(Android.Proguard.androidOptimizedRules),
                Android.Proguard.projectRules
            )
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = Android.KotlinOptions.jvmTargetVersion
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Android.ComposeOptions.kotlinCompilerExtensionVersion
    }

    packaging {
        resources {
            excludes += Android.PackagingOptions.license
            excludes += Android.PackagingOptions.native
            excludes += Android.PackagingOptions.dependencies
            excludes += Android.PackagingOptions.indexList
        }
    }

    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(projects.app.presentation)
    implementation(projects.foundation.uikit)
    implementation(platform(libs.compose.bom))
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