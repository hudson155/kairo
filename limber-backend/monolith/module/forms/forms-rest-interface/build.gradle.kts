plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:reps"))
  implementation(project(":limber-backend:common:rest-interface"))
  implementation(project(":limber-backend:common:serialization"))
  implementation(project(":limber-backend:common:util"))
  api(project(":limber-backend:monolith:common"))
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
