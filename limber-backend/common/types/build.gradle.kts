plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
