plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(kotlin("test"))
  api(kotlin("test-junit5"))
  implementation(project(":limber-backend:common:errors"))
  implementation(project(":limber-backend:common:exception-mapping"))
  implementation(project(":limber-backend:common:exceptions"))
  implementation(project(":limber-backend:common:reps"))
  implementation(project(":limber-backend:common:rest-interface"))
  implementation(project(":limber-backend:common:serialization"))
  implementation(project(":limber-backend:monolith:common:module"))
  api(Dependencies.JUnit.api)
  runtimeOnly(Dependencies.JUnit.engine)
  implementation(Dependencies.Jwt.auth0JavaJwt)
  api(Dependencies.Ktor.test)
  api(Dependencies.MockK.mockK)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
