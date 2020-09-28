plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(kotlin("test-junit5"))

  api(Dependencies.JUnit.api)
  runtimeOnly(Dependencies.JUnit.engine)
  api(Dependencies.MockK.mockK)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
