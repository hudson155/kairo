plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:util"))
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
