plugins {
    id("android-library-convention")
}

dependencies {
    api(projects.app.logic)
    api(projects.foundation.coroutines)
    api(projects.foundation.permissions)
    api(projects.foundation.inappreview)
    api(projects.foundation.controllers)
    implementation(libs.lifecycle.viewmodel.compose)
}