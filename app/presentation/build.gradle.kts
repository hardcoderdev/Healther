plugins {
    id("android-library-convention")
}

dependencies {
    api(projects.app.domain.logics)
    api(projects.foundation.permissions)
    api(projects.foundation.viewmodel)
    api(projects.foundation.controllers)
}