import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

plugins {
    kotlin("multiplatform") version Versions.kotlin
    kotlin("plugin.serialization") version Versions.kotlin
    id(Plugins.detekt).version(Versions.detekt)
}

repositories {
    jcenter()
}

tasks.create("downloadDependencies") {
    description = "Download all dependencies"
    doLast {
        configurations.forEach { if (it.isCanBeResolved) it.resolve() }
    }
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
    input = files(
        "src/commonMain/kotlin",
        "src/commonTest/kotlin",
        "src/jsMain/kotlin",
        "src/jsTest/kotlin",
        "src/jvmMain/kotlin",
        "src/jvmTest/kotlin"
    )
}

subprojects {

    buildscript {
        repositories {
            jcenter()
        }
    }

    repositories {
        jcenter()
    }

    tasks.withType<KotlinCompile<*>>().configureEach {
        (kotlinOptions as? KotlinJvmOptions)?.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlinx.serialization.ImplicitReflectionSerializer"
    }
}
