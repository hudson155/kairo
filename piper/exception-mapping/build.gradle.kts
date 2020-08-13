plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  implementation(project(":piper:errors"))
  implementation(project(":piper:exceptions"))
  implementation(project(":piper:util"))
  implementation(Dependencies.Ktor.httpJvm)
}

detekt {
  config = files("$rootDir/.detekt/config.yml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
