plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt")
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}
tasks.compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":piper:data-conversion"))
    implementation(Dependencies.Jackson.dataTypeJsr310)
    api(Dependencies.Jackson.moduleKotlin)
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
