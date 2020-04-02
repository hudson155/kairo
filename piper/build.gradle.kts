plugins {
    kotlin("jvm")
    id("io.gitlab.arturbosch.detekt").version(Versions.detekt)
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}
tasks.compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":piper:common"))
    api(project(":piper:errors"))
    api(project(":piper:exception-mapping"))
    implementation(Dependencies.Ktor.jackson)
    implementation(Dependencies.Ktor.serverHostCommon)
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
