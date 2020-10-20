plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(Dependencies.Jackson.databind)
  implementation(Dependencies.Jackson.datatypeJsr310)
  api(Dependencies.Jackson.moduleKotlin) // Not exposed by this module - provided to consumers.
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
