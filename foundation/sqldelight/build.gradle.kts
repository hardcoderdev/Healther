plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = Modules.Namespaces.Foundation.sqldelight
    compileSdk = Android.compileSdk

    defaultConfig {
        minSdk = Android.DefaultConfig.minSdk
    }
}

dependencies {
    api(projects.foundation.coroutines)
    implementation(libs.preferences.ktx)
    implementation(libs.sql.delight.driver)
    implementation(libs.sql.delight.coroutines.ext)
}