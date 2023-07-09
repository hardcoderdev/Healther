@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = BuildConstants.COMPOSE_COMPILER
}