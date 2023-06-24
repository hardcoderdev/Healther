plugins {
    id("android-library-convention")
    id("android-compose-convention")
}

dependencies {
    api(projects.foundation.controllers)
    implementation(platform(libs.compose.bom))
    implementation(libs.kotlin.datetime)
    implementation(libs.compose.activity)
    implementation(libs.compose.material)
    implementation(libs.compose.ui)
    implementation(libs.compose.extended.icons)
    implementation(libs.compose.charts)
    implementation(libs.compose.tooling)
    implementation(libs.compose.number.picker)
    implementation(libs.compose.calendar)
    implementation(libs.compose.accompanist.viewpager)
    implementation(libs.compose.accompanist.viewpager.indicators)
    implementation(libs.compose.lottie)
}