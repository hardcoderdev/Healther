plugins {
    id("android-library-convention")
    id("android-compose-convention")
}

dependencies {
    api(projects.foundation.controllers)
    api(projects.app.resources)
    api(projects.foundation.icons)
    api(projects.app.formatters)
    api(projects.app.data.mock)
    implementation(platform(libs.compose.bom))
    api(projects.foundation.datetime)
    implementation(libs.compose.activity)
    implementation(libs.compose.material)
    implementation(libs.compose.ui)
    implementation(libs.compose.extendedIcons)
    implementation(libs.compose.charts)
    implementation(libs.compose.tooling)
    implementation(libs.compose.numberPicker)
    implementation(libs.compose.calendar)
    implementation(libs.compose.accompanistViewpager)
    implementation(libs.compose.accompanistViewpagerIndicators)
    implementation(libs.compose.lottie)
}