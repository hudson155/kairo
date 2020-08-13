plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":piper:util"))
}

detekt {
  config = files("$rootDir/.detekt/config.yml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
