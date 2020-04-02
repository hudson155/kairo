plugins {
    kotlin("multiplatform") version Versions.kotlin
}

repositories {
    jcenter()
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
