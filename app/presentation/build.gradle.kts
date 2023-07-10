plugins {
    id("android-library-convention")
}

dependencies {
    api(projects.app.logic)
    api(projects.foundation.permissions)
    api(projects.foundation.controllers)
    api(libs.lifecycle.viewmodelCompose)
}