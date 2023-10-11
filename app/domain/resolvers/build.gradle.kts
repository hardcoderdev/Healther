plugins {
    id("android-library-convention")
}

dependencies {
    api(projects.app.domain.entities)
    api(projects.foundation.math)
}