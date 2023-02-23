@file:Suppress("UnstableApiUsage")

plugins {
    id(Plugins.Android.application)
    kotlin(Plugins.Kotlin.android)
    kotlin(Plugins.Kotlin.kapt)
    id(Plugins.serialization)
    id(Plugins.navigationSafeArgs)
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = Android.KotlinOptions.jvmTargetVersion
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Android.ComposeOptions.kotlinCompilerExtensionVersion
    }

    packagingOptions {
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
    implementation(project(Modules.Paths.di))
    implementation(project(Modules.Paths.uikit))
    addCompose()
    addCommonAndroid()
}