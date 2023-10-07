plugins {
    id("android-library-convention")
}

dependencies {
    api(projects.foundation.datetime)
    api(projects.app.ui.resources)
}