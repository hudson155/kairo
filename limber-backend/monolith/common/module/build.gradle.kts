plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:monolith:common"))
  api(project(":piper:module"))
  implementation(Dependencies.Logging.slf4j)
}

detekt {
  config = files("$rootDir/.detekt/config.yml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
