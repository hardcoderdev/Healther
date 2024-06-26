@file:Suppress("UnstableApiUsage")

import java.util.Properties

plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    id("android-compose-convention")
    id("android-app-convention")
}

project.rootProject.file("local.properties").reader().use {
    Properties().apply {
        load(it)
    }
}.onEach { (name, value) ->
    ext[name.toString()] = value
}

fun extString(name: String) = ext[name].toString()

android {
//    signingConfigs {
//        register("release") {
//            storeFile = file(extString("storeFile"))
//            storePassword = extString("storePassword")
//            keyAlias = extString("keyAlias")
//            keyPassword = extString("keyPassword")
//        }
//    }

    buildTypes {
        release {
           // signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
}

dependencies {
    implementation(projects.app.di)
    implementation(projects.app.ui.navigation)
    implementation(libs.activity.ktx)
    implementation(libs.bundles.koinDi)
    implementation(libs.core.ktx)
}