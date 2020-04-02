plugins {
    kotlin("multiplatform") version Versions.kotlin
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
}
