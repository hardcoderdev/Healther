plugins {
    id("android-library-convention")
    id("android-compose-convention")
}

dependencies {
    api(projects.app.ui.screens)
    api(libs.compose.navigation)
    api(libs.bundles.koinDi)
}