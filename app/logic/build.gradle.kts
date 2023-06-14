plugins {
    id("android-library-convention")
}

dependencies {
    api(projects.app.database)
    api(projects.foundation.datetime)
    api(projects.foundation.math)
    api(projects.foundation.sqldelight)
    api(projects.foundation.inappreview)
    implementation(libs.data.store)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.preferences.ktx)
    implementation(libs.sql.delight.driver)
    implementation(libs.sql.delight.coroutines.ext)
}