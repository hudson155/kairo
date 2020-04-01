dependencies {
    api(project(":piper"))
    api(Dependencies.JUnit.api)
    runtimeElements(Dependencies.JUnit.engine)
    api(Dependencies.Kotlin.test)
    api(Dependencies.Kotlin.testJunit5)
    api(Dependencies.Ktor.test)
    api(Dependencies.MockK.mockK)
}
