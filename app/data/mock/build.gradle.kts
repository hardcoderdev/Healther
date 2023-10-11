plugins {
    id("android-library-convention")
}

dependencies {
    implementation(projects.app.presentation)
    implementation(projects.app.ui.resources)
}