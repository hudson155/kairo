plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(Dependencies.Jackson.databind)
  implementation(Dependencies.Logging.slf4j)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
