plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  implementation(kotlin("reflect"))
  implementation(project(":limber-backend:common:reps"))
  implementation(project(":limber-backend:common:util"))
  implementation(Dependencies.Logging.slf4j)
  implementation(Dependencies.Ktor.serverCore)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
