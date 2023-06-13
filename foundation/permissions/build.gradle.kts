plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = Modules.Namespaces.Foundation.permissions
    compileSdk = Android.compileSdk

    defaultConfig {
        minSdk = Android.DefaultConfig.minSdk
    }
}

dependencies {
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.activity.ktx)
}