plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:module"))
  api(project(":limber-backend:monolith:common"))
  implementation(Dependencies.Logging.slf4j)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
