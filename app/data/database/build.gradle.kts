plugins {
    id("android-library-convention")
    alias(libs.plugins.sql.delight)
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("hardcoder.dev.database")
        }
    }
}

dependencies {
    api(projects.foundation.sqldelight)
    api(projects.foundation.datetime)
}