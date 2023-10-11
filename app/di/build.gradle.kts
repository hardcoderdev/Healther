plugins {
    id("android-library-convention")
}

dependencies {
    implementation(projects.app.ui.resources)
    implementation(projects.app.ui.pedometerManager)
    implementation(projects.app.ui.formatters)
    implementation(projects.app.ui.statisticsResolvers)
    implementation(projects.app.presentation)
    implementation(libs.bundles.koinDi)
}