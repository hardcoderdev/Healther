object Android {

    const val compileSdk = 34

    object PackagingOptions {
        const val license = "/META-INF/{AL2.0,LGPL2.1}"
        const val native = "/META-INF/native/*"
        const val indexList = "/META-INF/INDEX.LIST"
        const val dependencies = "META-INF/DEPENDENCIES"
    }

    object DefaultConfig {
        const val applicationId = "hardcoder.dev.healther"
        const val minSdk = 26
        const val targetSdk = 33

        const val instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        const val versionCode = 1
        const val versionName = "1.0"
    }

    object KotlinOptions {
        const val jvmTargetVersion = "17"
    }

    object ComposeOptions {
        const val kotlinCompilerExtensionVersion = "1.3.2"
    }

    object Proguard {
        const val androidOptimizedRules = "proguard-android-optimize.txt"
        const val projectRules = "proguard-rules.pro"
    }
}