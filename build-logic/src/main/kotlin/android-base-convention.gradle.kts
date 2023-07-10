import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("code-smell-convention")
    id("code-style-convention")
}

configure<BaseExtension> {
    namespace = buildAndroidNamespace()
    compileSdkVersion(BuildConstants.COMPILE_ANDROID_SDK)

    defaultConfig {
        archivesName.set("${BuildConstants.ANDROID_APP_ID}-${BuildConstants.APP_VERSION_NAME}")
        if (this@configure is BaseAppModuleExtension) {
            applicationId = BuildConstants.ANDROID_APP_ID
        }
        if (this@configure is BaseAppModuleExtension) {
            resourceConfigurations.addAll(
                arrayOf(
                    "en",
                    "ru",
                ),
            )
        }
        minSdk = BuildConstants.MIN_ANDROID_SDK
        targetSdk = BuildConstants.TARGET_ANDROID_SDK
        versionCode = BuildConstants.APP_VERSION_CODE
        versionName = BuildConstants.APP_VERSION_NAME
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(BuildConstants.JVM_TARGET)
        targetCompatibility = JavaVersion.toVersion(BuildConstants.JVM_TARGET)
    }

    if (this is BaseAppModuleExtension) {
        bundle {
            storeArchive {
                enable = true
            }
        }
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = BuildConstants.JVM_TARGET
    }
}

fun Project.buildAndroidNamespace(): String {
    val pathToModule = project.path.replace(
        regex = "[:\\-]".toRegex(),
        replacement = ".",
    )

    return (BuildConstants.PROJECT_DEVELOPER_NAME + "." + rootProject.name + pathToModule).lowercase()
}