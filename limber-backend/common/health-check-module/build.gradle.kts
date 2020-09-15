plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id(Plugins.detekt)
}

dependencies {
  implementation(project(":limber-backend:common:reps"))
  implementation(project(":limber-backend:monolith:common:module"))
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
