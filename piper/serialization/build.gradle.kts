plugins {
    kotlin("multiplatform")
    id(Plugins.detekt)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(kotlin("reflect"))
                implementation(project(":piper:data-conversion"))
                implementation(project(":piper:types"))
                api(Dependencies.Serialization.common)
            }
        }
        jvm().compilations["main"].defaultSourceSet {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                api(Dependencies.Serialization.jvm)
            }
        }
        js().compilations["main"].defaultSourceSet  {
            dependencies {
                implementation(kotlin("stdlib-js"))
                api(Dependencies.Serialization.js)
            }
        }
    }
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
