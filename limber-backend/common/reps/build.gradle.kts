plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:validation")) // Not used by this module - provided to consumers.

  api(Dependencies.Jackson.annotations) // Not used by this module - provided to consumers.
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
