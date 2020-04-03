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
                implementation(project(":piper:types"))
                implementation(project(":piper:validation"))
                api(Dependencies.Serialization.common)
            }
        }
        jvm().compilations["main"].defaultSourceSet {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(project(":piper:util"))
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
