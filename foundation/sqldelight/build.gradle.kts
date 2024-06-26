plugins {
    id("android-library-convention")
}

dependencies {
    api(projects.foundation.coroutines)
    api(libs.sqlDelight.primitiveAdapters)
    api(libs.sqlDelight.androidDriver)
    api(libs.sqlDelight.coroutinesExt)
}