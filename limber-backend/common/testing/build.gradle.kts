plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(kotlin("test"))
  api(kotlin("test-junit5"))
  api(project(":limber-backend:common:application")) // Tests application (so do implementations)
  implementation(project(":limber-backend:common:errors")) // Parses errors internally
  implementation(project(":limber-backend:common:exception-mapping")) // Used for expected exceptions/errors
  implementation(project(":limber-backend:common:module")) // Tests application (so do implementations)
  implementation(project(":limber-backend:common:reps"))
  api(project(":limber-backend:common:serialization")) // Includes Json in interface (so do implementations)
  runtimeOnly(Dependencies.JUnit.engine)
  api(Dependencies.Ktor.test)
  api(Dependencies.MockK.mockK)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
