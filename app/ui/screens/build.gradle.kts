plugins {
    id("android-library-convention")
    id("android-compose-convention")
}

dependencies {
    api(libs.bundles.koinDi)
    api(projects.app.presentation)
    api(projects.app.ui.pedometerManager)
    api(projects.foundation.uikit)
    api(projects.app.ui.statisticsResolvers)
    api(libs.moko.resourcesCompose)
    api(platform(libs.compose.bom))
    api(libs.compose.activity)
    api(libs.compose.graphics)
    api(libs.compose.tooling)
    api(libs.compose.accompanistFlowrow)
    api(libs.compose.coil)
    api(libs.compose.lottie)
    debugApi(libs.compose.debugTooling)
}