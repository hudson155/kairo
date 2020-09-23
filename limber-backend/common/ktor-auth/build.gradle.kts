plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(Dependencies.Ktor.auth)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
