plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id(Plugins.detekt)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                api(project(":limber-backend-application:common:interface"))
                api(project(":piper:reps"))
                implementation(project(":piper:serialization"))
                api(project(":piper:types"))
            }
        }
        jvm {
            compilations["main"].defaultSourceSet {
                dependencies {
                    implementation(kotlin("stdlib-jdk8"))
                }
            }
        }
        js {
            browser { }
            compilations["main"].defaultSourceSet {
                dependencies {
                    implementation(kotlin("stdlib-js"))
                }
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
