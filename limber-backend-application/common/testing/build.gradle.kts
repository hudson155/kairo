plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":limber-backend-application"))
    api(project(":limber-backend-application:common:module"))
    api(project(":piper:testing"))
    implementation(Dependencies.Jwt.auth0JavaJwt)
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
    input = files("src/main/kotlin", "src/test/kotlin")
}
