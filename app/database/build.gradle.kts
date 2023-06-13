plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.sql.delight)
}

android {
    namespace = Modules.Namespaces.database
    compileSdk = Android.compileSdk

    defaultConfig {
        minSdk = Android.DefaultConfig.minSdk
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "hardcoder.dev.database"
        dialect = "sqlite:3.24"
    }
}

dependencies {
    implementation(libs.preferences.ktx)
    implementation(libs.sql.delight.driver)
    implementation(libs.sql.delight.coroutines.ext)
    implementation(libs.kotlin.datetime)
}