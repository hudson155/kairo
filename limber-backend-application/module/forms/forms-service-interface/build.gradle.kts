plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  implementation(project(":limber-backend-application:common"))
  api(project(":piper:finder"))
}

detekt {
  config = files("$rootDir/.detekt/config.yml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
