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
            }
        }
        jvm {
            compilations["main"].defaultSourceSet {
                dependencies {
                    implementation(kotlin("stdlib-jdk8"))
                    implementation(Dependencies.Jackson.dataTypeJsr310)
                    implementation(Dependencies.Jackson.moduleKotlin)
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
