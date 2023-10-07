plugins {
    id("android-library-convention")
}

dependencies {
    api(projects.app.data.database)
    api(projects.app.domain.validators)
    api(projects.app.domain.resolvers)
    api(projects.app.domain.mappers)
    api(projects.foundation.icons)
}