plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(kotlin("test"))
  api(kotlin("test-junit5"))

  implementation(project(":limber-backend:common:exception-mapping"))
  implementation(project(":limber-backend:common:module"))
  implementation(project(":limber-backend:common:reps"))
  api(project(":limber-backend:common:serialization"))
  api(project(":limber-backend:common:server"))

  runtimeOnly(Dependencies.JUnit.engine)
  api(Dependencies.Ktor.test)
  api(Dependencies.MockK.mockK)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
