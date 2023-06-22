plugins {
    id("android-library-convention")
}

dependencies {
    api(projects.foundation.coroutines)
    implementation(libs.preferences.ktx)
    implementation(libs.sql.delight.android.driver)
    implementation(libs.sql.delight.coroutines.ext)
}