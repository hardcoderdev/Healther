plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = Modules.Namespaces.Foundation.datetime
    compileSdk = Android.compileSdk

    defaultConfig {
        multiDexEnabled = true
        minSdk = Android.DefaultConfig.minSdk
    }
}

dependencies {
    api(libs.kotlin.datetime)
    api(libs.compose.calendar.datetime)
}