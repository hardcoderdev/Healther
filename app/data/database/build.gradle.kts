plugins {
    id("android-library-convention")
    alias(libs.plugins.kotlin.kapt)
}

dependencies {
    api(projects.foundation.datetime)
    kapt(libs.room.compiler)
    api(libs.room.runtime)
    api(libs.room.ktx)
    androidTestApi(libs.room.testing)
}