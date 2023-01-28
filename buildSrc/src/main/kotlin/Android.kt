object Android {

    const val compileSdk = 33

    object Classpaths {
        object Versions {
            const val kotlin = "1.7.20"
            const val gradle = "8.0.0-alpha10"
            const val navigation = "2.4.2"
        }

        const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        const val navigation = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    }

    object PackagingOptions {
        const val license = "/META-INF/{AL2.0,LGPL2.1}"
        const val native = "/META-INF/native/*"
        const val indexList = "/META-INF/INDEX.LIST"
        const val dependencies = "META-INF/DEPENDENCIES"
    }

    object Repositories {
        const val jitpack = "https://jitpack.io"
    }

    object DefaultConfig {
        const val applicationId = "hardcoder.dev.healther"
        const val minSdk = 24
        const val targetSdk = 33

        const val instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        const val versionCode = 1
        const val versionName = "1.0"
    }

    object KotlinOptions {
        const val jvmTargetVersion = "11"
    }

    object ComposeOptions {
        const val kotlinCompilerExtensionVersion = "1.3.2"
    }

    object Proguard {
        const val androidOptimizedRules = "proguard-android-optimize.txt"
        const val projectRules = "proguard-rules.pro"
    }
}