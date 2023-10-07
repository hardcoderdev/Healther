plugins {
    id("android-library-convention")
    id("android-compose-convention")
}

dependencies {
    api(projects.app.ui.screens)
    api(libs.bundles.koinDi)
    api(libs.bundles.voyager.navigation)
}