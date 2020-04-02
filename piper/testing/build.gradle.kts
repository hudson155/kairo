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
    api(project(":piper"))
    api(Dependencies.JUnit.api)
    runtimeElements(Dependencies.JUnit.engine)
    api(Dependencies.Kotlin.test)
    api(Dependencies.Kotlin.testJunit5)
    api(Dependencies.Ktor.test)
    api(Dependencies.MockK.mockK)
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
