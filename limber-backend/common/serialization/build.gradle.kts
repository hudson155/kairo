plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  implementation(project(":limber-backend:common:data-conversion"))
  implementation(project(":limber-backend:common:types"))
  implementation(Dependencies.Jackson.datatypeJsr310)
  implementation(Dependencies.Jackson.moduleKotlin)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
