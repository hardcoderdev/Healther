plugins {
    id("android-library-convention")
}

dependencies {
    api(projects.app.data.database)
    api(projects.foundation.icons)
    api(projects.foundation.datetime)
    api(projects.foundation.math)
    api(projects.foundation.inappreview)
}