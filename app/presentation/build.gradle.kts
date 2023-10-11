plugins {
    id("android-library-convention")
}

dependencies {
    api(projects.app.domain.logics)
    api(projects.foundation.permissions)
    api(libs.voyager.koin)
    api(projects.foundation.controllers)
}