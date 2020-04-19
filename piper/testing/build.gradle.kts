plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(kotlin("test"))
    api(kotlin("test-junit5"))
    api(project(":piper:application")) // Tests application (so do implementations)
    implementation(project(":piper:errors")) // Parses errors internally
    implementation(project(":piper:exception-mapping")) // Used for expected exceptions/errors
    implementation(project(":piper:module")) // Tests application (so do implementations)
    api(project(":piper:serialization")) // Includes Json in interface (so do implementations)
    runtimeOnly(Dependencies.JUnit.engine)
    api(Dependencies.Ktor.test)
    api(Dependencies.MockK.mockK)
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
    input = files("src/main/kotlin", "src/test/kotlin")
}
