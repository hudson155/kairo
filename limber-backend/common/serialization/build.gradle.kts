plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  implementation(project(":limber-backend:common:type-conversion"))

  api(Dependencies.Jackson.datatypeJsr310)
  api(Dependencies.Jackson.moduleKotlin)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
