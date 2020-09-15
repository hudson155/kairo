plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:sql"))
  implementation(project(":limber-backend:monolith:common"))
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
