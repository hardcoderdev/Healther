plugins {
    id("android-library-convention")
}

dependencies {
    api(projects.app.database)
    api(projects.foundation.datetime)
    api(projects.foundation.math)
    api(projects.foundation.inappreview)
}