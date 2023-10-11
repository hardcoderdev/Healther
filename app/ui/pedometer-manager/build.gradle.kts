plugins {
    id("android-library-convention")
}

dependencies {
    api(projects.foundation.coroutines)
    api(projects.app.ui.resources)
    api(projects.app.presentation)
    api(projects.foundation.permissions)
    api(libs.activity.ktx)
    api(libs.bundles.koinDi)
}