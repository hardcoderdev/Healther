plugins {
    id("android-library-convention")
    alias(libs.plugins.sql.delight)
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