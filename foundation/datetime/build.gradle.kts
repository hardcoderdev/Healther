plugins {
    id("android-library-convention")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(projects.foundation.coroutines)
    api(libs.kotlin.datetime)
    api(libs.kotlin.serialization)
}