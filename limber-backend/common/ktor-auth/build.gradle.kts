plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(Dependencies.Ktor.auth) // Provides Ktor auth along with this artifact
}

detekt {
  config = files("$rootDir/.detekt/config.yml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
