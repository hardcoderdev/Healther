plugins {
    id("android-library-convention")
    alias(libs.plugins.sql.delight)
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("hardcoder.dev.database")
        }
    }
}

dependencies {
    implementation(libs.preferences.ktx)
    implementation(libs.sql.delight.android.driver)
    implementation(libs.sql.delight.coroutines.ext)
    implementation(libs.sql.delight.primitive.adapters)
    implementation(libs.kotlin.datetime)
}