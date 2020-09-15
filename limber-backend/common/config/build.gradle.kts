plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  implementation(Dependencies.Jackson.annotations)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
