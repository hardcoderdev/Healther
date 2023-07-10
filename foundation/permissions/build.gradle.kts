plugins {
    id("android-library-convention")
}

dependencies {
    api(projects.foundation.coroutines)
    implementation(libs.activity.ktx)
}