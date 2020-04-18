import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

plugins {
    kotlin("multiplatform") version Versions.kotlin
    kotlin("plugin.serialization") version Versions.kotlin
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
