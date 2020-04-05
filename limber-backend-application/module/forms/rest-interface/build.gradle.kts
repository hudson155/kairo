plugins {
    kotlin("multiplatform")
    id(Plugins.detekt)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(project(":piper:reps"))
                implementation(project(":piper:types"))
            }
        }
        jvm().compilations["main"].defaultSourceSet {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        js().compilations["main"].defaultSourceSet {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
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
